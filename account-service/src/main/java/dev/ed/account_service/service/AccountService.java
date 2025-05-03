package dev.ed.account_service.service;

import dev.ed.account_service.DTOs.DetailsUpdateDTO;
import dev.ed.account_service.exception.InsufficientFundsException;
import dev.ed.account_service.model.Account;
import dev.ed.account_service.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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

    public BigDecimal getBalance(UUID accountId) throws EntityNotFoundException {
        Account account = getAccountDetails(accountId);
        return account.getBalance();
    }

    public Account deposit(UUID accountId, BigDecimal amount) throws EntityNotFoundException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        Account account = getAccountDetails(accountId);
        BigDecimal updatedBalance = account.getBalance().add(amount);
        account.setBalance(updatedBalance);
        return accountRepository.save(account);
    }

    public Account withdraw(UUID accountId, BigDecimal amount) throws EntityNotFoundException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        Account account = getAccountDetails(accountId);
        BigDecimal updatedBalance = account.getBalance().subtract(amount);

        if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        account.setBalance(updatedBalance);
        return accountRepository.save(account);
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
}
