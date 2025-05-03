package dev.ed.account_service.controller;

import dev.ed.account_service.DTOs.*;
import dev.ed.account_service.helper.AccountGenerator;
import dev.ed.account_service.helper.AccountMapper;
import dev.ed.account_service.model.Account;
import dev.ed.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final AccountGenerator accountGenerator;


    @PostMapping
    public ResponseEntity<AccountDetailsResponseDTO> createAccount(@RequestBody CreateAccountDTO accountRequest) {
        Account createdAccount = accountService.createAccount(accountMapper.toAccountEntity(accountRequest));
        return new ResponseEntity<>(accountMapper.toResponseDTO(createdAccount), HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDetailsResponseDTO> getAccountDetails(@PathVariable UUID accountId) {
        Account account = accountService.getAccountDetails(accountId);
        return new ResponseEntity<>(accountMapper.toResponseDTO(account), HttpStatus.OK);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable UUID accountId) {
        BigDecimal balance = accountService.getBalance(accountId);
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setBalance(balance);
        return new ResponseEntity<>(balanceDTO, HttpStatus.OK);
    }

    @PostMapping("/{accountId}/balance/deposit")
    public ResponseEntity<AccountDetailsResponseDTO> depositAmount(@RequestBody DepositWithdrawDTO depositRequest) {
        Account updatedAccount = accountService.deposit(depositRequest.getAccountId(), depositRequest.getAmount());
        return new ResponseEntity<>(accountMapper.toResponseDTO(updatedAccount), HttpStatus.OK);
    }

    @PostMapping("/{accountId}/balance/withdraw")
    public ResponseEntity<AccountDetailsResponseDTO> withdrawAmount(@RequestBody DepositWithdrawDTO withdrawRequest) {
        Account updatedAccount = accountService.withdraw(withdrawRequest.getAccountId(), withdrawRequest.getAmount());
        return new ResponseEntity<>(accountMapper.toResponseDTO(updatedAccount), HttpStatus.OK);
    }

    @PatchMapping("/{accountId}/status")
    public ResponseEntity<AccountDetailsResponseDTO> updateAccountDetails(@RequestBody DetailsUpdateDTO detailsUpdateRequest) {
        Account updatedAccount = accountService.updateAccountDetails(detailsUpdateRequest.getAccountId(), detailsUpdateRequest);
        return new ResponseEntity<>(accountMapper.toResponseDTO(updatedAccount), HttpStatus.OK);
    }

    @GetMapping("/listAccounts")
    public ResponseEntity<List<AccountDetailsResponseDTO>> listAccounts() {
        List<AccountDetailsResponseDTO> accounts = accountService.getListOfAccounts().stream().map(accountMapper::toResponseDTO).collect(Collectors.toList());
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @DeleteMapping("{accountId}/delete")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID accountId) {
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>("Account: " + accountId + " Deleted", HttpStatus.OK);
    }

    @PostMapping("/TEST/batch/generateAccount")
    public ResponseEntity<String> generateAccounts(@RequestParam(defaultValue = "1") int numberOfAccounts) {
        for (int i = 0; i < numberOfAccounts; i++) {
            accountGenerator.generateAccount();
        }
        return new ResponseEntity<>(numberOfAccounts + " Accounts Created", HttpStatus.OK);
    }

    @DeleteMapping("TEST/batch/deleteAccount")
    public ResponseEntity<String> deleteAccounts(@RequestBody List<UUID> accountsList) {
        for (UUID account : accountsList) {
            accountService.deleteAccount(account);
        }
        return new ResponseEntity<>(accountsList.size() + " accounts have been deleted", HttpStatus.OK);
    }
}
