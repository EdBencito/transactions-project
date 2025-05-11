package dev.ed.account_service.controller;

import dev.ed.account_service.DTOs.AccountDetailsResponseDTO;
import dev.ed.account_service.DTOs.BalanceDTO;
import dev.ed.account_service.DTOs.CreateAccountDTO;
import dev.ed.account_service.DTOs.DetailsUpdateDTO;
import dev.ed.account_service.helper.AccountGenerator;
import dev.ed.account_service.helper.AccountMapper;
import dev.ed.account_service.model.Account;
import dev.ed.account_service.service.AccountService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/accounts")
public class InternalAccountController {

    private final AccountService accountService;
    private final AccountGenerator accountGenerator;

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID accountId) {
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>("Account: " + accountId + " Deleted", HttpStatus.OK);
    }

    @PostMapping("/batch/generate")
    public ResponseEntity<String> generateAccounts(@RequestParam(defaultValue = "1") int numberOfAccounts) {
        for (int i = 0; i < numberOfAccounts; i++) {
            accountGenerator.generateAccount();
        }
        return new ResponseEntity<>(numberOfAccounts + " Accounts Created", HttpStatus.CREATED);
    }

    @DeleteMapping("/batch/delete")
    public ResponseEntity<String> deleteAccounts(@RequestBody List<UUID> accountsList) {
        for (UUID accountId : accountsList) {
            accountService.deleteAccount(accountId);
        }
        return new ResponseEntity<>(accountsList.size() + " accounts have been deleted", HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/randomAccountId")
    public ResponseEntity<UUID> getRandomAccountId() {
        UUID randomAccountId = accountService.getRandomAccountId().orElseThrow(() -> new EntityNotFoundException("No Accounts Found"));
        return new ResponseEntity<>(randomAccountId, HttpStatus.OK);
    }
}
