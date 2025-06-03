package dev.ed.account_service.service;

import dev.ed.account_service.DTOs.DetailsUpdateDTO;
import dev.ed.account_service.client.TransactionClient;
import dev.ed.account_service.helper.AccountMapper;
import dev.ed.account_service.model.Account;
import dev.ed.account_service.repository.AccountRepository;
import dev.ed.avro.BalanceUpdateEvent;
import dev.ed.avro.TransactionType;
import dev.ed.shared.DTOs.TransactionDetailsUpdateDTO;
import dev.ed.shared.enums.TransactionStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final TransactionClient transactionClient;
    private final AccountRepository accountRepository;


    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountDetails(UUID accountId) throws EntityNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
    }

    public Account updateAccountDetails(UUID accountId, DetailsUpdateDTO newDetails) throws EntityNotFoundException {
        Account account = getAccountDetails(accountId);
        account.setAccountStatus(newDetails.getAccountStatus() != null ? newDetails.getAccountStatus() : account.getAccountStatus());
        account.setCurrency(newDetails.getCurrency() != null ? newDetails.getCurrency() : account.getCurrency());
        account.setInterestRate(newDetails.getInterestRate() != null ? newDetails.getInterestRate() : account.getInterestRate());
        account.setCreditLimit(newDetails.getCreditLimit() != null ? newDetails.getCreditLimit() : account.getCreditLimit());
        return accountRepository.save(account);
    }

    public List<Account> getListOfAccounts() {
        return accountRepository.findAll();
    }

    public void deleteAccount(UUID accountId) {
        accountRepository.deleteById(accountId);
    }

    public Optional<UUID> getRandomAccountId() {
        return accountRepository.findRandomAccountId();
    }

    @KafkaListener(topics = "${app.kafka.topic.transaction-service-update}", groupId = "account-service")
    public void handleProcessedEvent(BalanceUpdateEvent event) {
        System.out.println("event Received");
        updateBalance(event);
    }

    private void updateBalance(BalanceUpdateEvent event) {
        try {
            Account account = getAccountDetails(UUID.fromString(event.getAccountId()));
            switch (event.getTransactionType()) {
                case CREDIT -> {
                    account.setBalance(account.getBalance().add(event.getAmount()));
                    System.out.println("Account: " + event.getAccountId() + " balance has been changed due to transaction:" + event.getTransactionId() + " with an amount of:" + event.getAmount() + " " + event.getTransactionType());

                }
                case DEBIT -> {
                    account.setBalance(account.getBalance().subtract(event.getAmount()));
                    System.out.println("Account: " + event.getAccountId() + " balance has been changed due to transaction:" + event.getTransactionId() + " with an amount of:" + event.getAmount() + " " + event.getTransactionType());

                }
                case REVERSAL -> {
                    if (event.getTransactionType() == TransactionType.DEBIT) {
                        account.setBalance(account.getBalance().add(event.getAmount()));
                        System.out.println("Account: " + event.getAccountId() + " balance has been changed due to flagged transaction:" + event.getTransactionId() + " with an amount of:" + event.getAmount() + " " + TransactionType.CREDIT);

                    } else if (event.getTransactionType() == TransactionType.CREDIT) {
                        account.setBalance(account.getBalance().subtract(event.getAmount()));
                        System.out.println("Account: " + event.getAccountId() + " balance has been changed due to flagged transaction:" + event.getTransactionId() + " with an amount of:" + event.getAmount() + " " + TransactionType.DEBIT);
                    }
                    TransactionDetailsUpdateDTO updateDTO = new TransactionDetailsUpdateDTO();
                    updateDTO.setTransactionId(UUID.fromString(event.getTransactionId()));
                    updateDTO.setTransactionStatus(TransactionStatus.valueOf("REVERSED"));
                    transactionClient.updateTransactionDetails(accountMapper.toTransactionUpdateDetailsDTO(updateDTO));
                }
                default ->
                        throw new IllegalArgumentException("Unsupported transaction type: " + event.getTransactionType());
            }
            accountRepository.save(account);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            System.out.println("Error updating balance" + e.getMessage());
        }
    }
}
