package dev.ed.account_service.service;

import dev.ed.account_service.DTOs.DetailsUpdateDTO;
import dev.ed.account_service.model.Account;
import dev.ed.account_service.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountService {

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
}
