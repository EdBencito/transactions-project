package dev.ed.account_service.controller;

import dev.ed.account_service.DTOs.AccountDetailsResponseDTO;
import dev.ed.account_service.DTOs.BalanceDTO;
import dev.ed.account_service.DTOs.CreateAccountDTO;
import dev.ed.account_service.DTOs.DetailsUpdateDTO;
import dev.ed.account_service.helper.AccountMapper;
import dev.ed.account_service.model.Account;
import dev.ed.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;


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
        Account account = accountService.getAccountDetails(accountId);
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setAccountId(account.getAccountId());
        balanceDTO.setBalance(account.getBalance());
        return new ResponseEntity<>(balanceDTO, HttpStatus.OK);
    }

    @PatchMapping("/{accountId}/accountDetails")
    public ResponseEntity<AccountDetailsResponseDTO> updateAccountDetails(@RequestBody DetailsUpdateDTO detailsUpdateRequest) {
        Account updatedAccount = accountService.updateAccountDetails(detailsUpdateRequest.getAccountId(), detailsUpdateRequest);
        return new ResponseEntity<>(accountMapper.toResponseDTO(updatedAccount), HttpStatus.OK);
    }

    @GetMapping("/listAccounts")
    public ResponseEntity<List<AccountDetailsResponseDTO>> listAccounts() {
        List<AccountDetailsResponseDTO> accounts = accountService.getListOfAccounts().stream().map(accountMapper::toResponseDTO).collect(Collectors.toList());
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

}
