package dev.ed.account_service.helper;

import dev.ed.shared.DTOs.AccountDetailsResponseDTO;
import dev.ed.account_service.DTOs.CreateAccountDTO;
import dev.ed.account_service.exception.MaxRetriesException;
import dev.ed.account_service.model.Account;
import dev.ed.account_service.repository.AccountRepository;
import dev.ed.shared.DTOs.TransactionDetailsResponseDTO;
import dev.ed.shared.DTOs.TransactionDetailsUpdateDTO;
import dev.ed.shared.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Component
public class AccountMapper {
    private final AccountRepository accountRepository;

    public AccountDetailsResponseDTO toResponseDTO(Account account) {
        return AccountDetailsResponseDTO.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .accountStatus(account.getAccountStatus())
                .accountHolderName(account.getAccountHolderName())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .interestRate(account.getInterestRate())
                .creditLimit(account.getCreditLimit())
                .build();
    }

    public Account toAccountEntity(CreateAccountDTO dto) {
        return Account.builder()
                .accountId(generateUUID())
                .accountNumber(generateAccountNumber())
                .accountType(dto.getAccountType())
                .accountStatus(AccountStatus.ACTIVE)
                .accountHolderName(dto.getAccountHolderName())
                .openingDate(LocalDate.now())
                .currency("GBP")
                .balance(dto.getBalance() != null ? dto.getBalance() : BigDecimal.valueOf(0.00))
                .interestRate(dto.getInterestRate() != null ? dto.getInterestRate() : BigDecimal.valueOf(4.5))
                .creditLimit(dto.getCreditLimit() != null ? dto.getCreditLimit() : BigDecimal.valueOf(4000))
                .build();
    }

    public TransactionDetailsResponseDTO toTransactionResponseDTO(TransactionDetailsResponseDTO transactionDetailsResponseDTO) {
        return  TransactionDetailsResponseDTO.builder()
                .transactionId(transactionDetailsResponseDTO.getTransactionId())
                .accountId(transactionDetailsResponseDTO.getAccountId())
                .transactionStatus(transactionDetailsResponseDTO.getTransactionStatus())
                .creationDate(transactionDetailsResponseDTO.getCreationDate())
                .lastUpdated(transactionDetailsResponseDTO.getLastUpdated())
                .transactionType(transactionDetailsResponseDTO.getTransactionType())
                .currency(transactionDetailsResponseDTO.getCurrency())
                .amount(transactionDetailsResponseDTO.getAmount())
                .merchantCategory(transactionDetailsResponseDTO.getMerchantCategory())
                .transactionChannel(transactionDetailsResponseDTO.getTransactionChannel())
                .isFraudulent(transactionDetailsResponseDTO.isFraudulent())
                .build();
    }

    public TransactionDetailsUpdateDTO toTransactionUpdateDetailsDTO(TransactionDetailsUpdateDTO transactionDetailsUpdateDTO) {
        return  TransactionDetailsUpdateDTO.builder()
                .transactionId(transactionDetailsUpdateDTO.getTransactionId())
                .transactionStatus(transactionDetailsUpdateDTO.getTransactionStatus())
                .build();
    }

    public String generateAccountNumber() {
        int maxRetries = 5;
        int retryCount = 0;
        String accountNumber;
        do {
            accountNumber = String.valueOf(ThreadLocalRandom.current().nextLong(10_000_000_000L, 100_000_000_000L));
            retryCount++;
        } while (accountRepository.existsByAccountNumber(accountNumber) && retryCount < maxRetries); // Retry if exists
        if (retryCount == maxRetries)
            throw new MaxRetriesException("Max Retries for accountNumber generation has been hit");
        return accountNumber;
    }

    public UUID generateUUID() {
        int maxRetries = 5;
        int retryCount = 0;
        UUID accountId;
        do {
            accountId = UUID.randomUUID();
            retryCount++;
        } while (accountRepository.existsById(accountId) && retryCount < maxRetries);
        if (retryCount == maxRetries)
            throw new MaxRetriesException("Max Retries for accountId generation has been hit");// Retry if exists
        return accountId;
    }

}
