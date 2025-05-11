package dev.ed.transaction_service.controller;

import dev.ed.transaction_service.helper.TransactionGenerator;
import dev.ed.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/transaction")
public class InternalTransactionController {

    private final TransactionService transactionService;
    private final TransactionGenerator transactionGenerator;

    @PostMapping("/batch/generate")
    public ResponseEntity<String> generateTransactions(@RequestParam(defaultValue = "1") int numberOfAccounts) {
        for (int i = 0; i < numberOfAccounts; i++) {
            transactionGenerator.generateTransactions();
        }
        return new ResponseEntity<>(numberOfAccounts + " Transactions Created", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{transactionId}")
    public ResponseEntity<String> deleteTransactions(@PathVariable UUID transactionId) {
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>("transaction: " + transactionId + " Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/batch/delete")
    public ResponseEntity<String> deleteAccounts(@RequestBody List<UUID> accountsList) {
        for (UUID account : accountsList) {
            transactionService.deleteTransaction(account);
        }
        return new ResponseEntity<>(accountsList.size() + " accounts have been deleted", HttpStatus.OK);
    }
}
