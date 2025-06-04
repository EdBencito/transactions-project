package dev.ed.transaction_service.controller;

import dev.ed.transaction_service.helper.TransactionGenerator;
import dev.ed.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/transaction")
public class InternalTransactionController {

    private final TransactionService transactionService;
    private final TransactionGenerator transactionGenerator;

    @PostMapping("/batch/generate")
    public ResponseEntity<String> generateTransactions(@RequestParam(defaultValue = "1") int numberOfTransactions) {
        for (int i = 0; i < numberOfTransactions; i++) {
            transactionGenerator.generateTransactions();
        }
        return new ResponseEntity<>(numberOfTransactions + " Transactions Created", HttpStatus.CREATED);
    }

    @PostMapping("/batch/generate/fraudulent")
    public ResponseEntity<String> generateFraudulentTransactions(@RequestParam(defaultValue = "1") int numberOfTransactions) {
        for (int i = 0; i < numberOfTransactions; i++) {
            transactionGenerator.generateFraudulentTransaction();
        }
        return new ResponseEntity<>(numberOfTransactions + " Fraudulent Transactions Created", HttpStatus.CREATED);
    }

    @PostMapping("/generate-random")
    public ResponseEntity<String> generateRandomTransaction(@RequestParam(defaultValue = "1") int numberOfTransactions) {

        for (int i = 0; i < numberOfTransactions; i++) {
            if (new Random().nextBoolean()) {
                transactionGenerator.generateTransactions();
            } else {
                transactionGenerator.generateFraudulentTransaction();
            }

        }
        return new ResponseEntity<>(numberOfTransactions + " Random Transactions Created", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{transactionId}")
    public ResponseEntity<String> deleteTransactions(@PathVariable UUID transactionId) {
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>("transaction: " + transactionId + " Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/batch/delete")
    public ResponseEntity<String> batchDeleteTransactions(@RequestBody List<UUID> transactionsList) {
        for (UUID transaction : transactionsList) {
            transactionService.deleteTransaction(transaction);
        }
        return new ResponseEntity<>(transactionsList.size() + " transactions have been deleted", HttpStatus.OK);
    }
}
