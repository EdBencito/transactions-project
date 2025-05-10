package dev.ed.transaction_service.controller;

import dev.ed.transaction_service.DTOs.CreateTransactionDTO;
import dev.ed.transaction_service.DTOs.TransactionDetailsResponseDTO;
import dev.ed.transaction_service.DTOs.TransactionFiltersDTO;
import dev.ed.transaction_service.helper.TransactionGenerator;
import dev.ed.transaction_service.helper.TransactionMapper;
import dev.ed.transaction_service.model.Transaction;
import dev.ed.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {


    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final TransactionGenerator transactionGenerator;

    @PostMapping
    public ResponseEntity<TransactionDetailsResponseDTO> createTransaction(@RequestBody CreateTransactionDTO transactionRequest) {
        Transaction createdTransaction = transactionService.createTransaction(transactionMapper.toTransactionEntity(transactionRequest));
        return new ResponseEntity<>(transactionMapper.toResponseDTO(createdTransaction), HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDetailsResponseDTO> getTransaction(@PathVariable UUID transactionId) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        return new ResponseEntity<>(transactionMapper.toResponseDTO(transaction), HttpStatus.OK);
    }

    @GetMapping("/listTransactions/filters")
    public ResponseEntity<List<TransactionDetailsResponseDTO>> getAllTransactionsByFilter(@Valid @RequestBody TransactionFiltersDTO transactionFilters) {
        List<TransactionDetailsResponseDTO> transactions = transactionService.getAllTransactionsByFilters(
                transactionFilters.getAccountId(),
                transactionFilters.getTransactionStatus(),
                transactionFilters.getCreationDate(),
                transactionFilters.getLastUpdated()
                ).stream().map(transactionMapper::toResponseDTO).collect(Collectors.toList());
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }

    @GetMapping("/{accountId}/listTransactions")
    public ResponseEntity<List<TransactionDetailsResponseDTO>> getAllTransactionsByAccountId(@PathVariable UUID accountId) {
        List<TransactionDetailsResponseDTO> transactions = transactionService.getAllTransactionsByAccountId(accountId)
                .stream().map(transactionMapper::toResponseDTO).collect(Collectors.toList());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}/delete")
    public ResponseEntity<String> deleteTransactions(@PathVariable UUID transactionId) {
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>("transaction: " + transactionId + " Deleted", HttpStatus.OK);
    }

    @PostMapping("/TEST/batch/generateTransaction")
    public ResponseEntity<String> generateTransactions(@RequestParam(defaultValue = "1") int numberOfAccounts) {
        for (int i = 0; i < numberOfAccounts; i++) {
            transactionGenerator.generateTransactions();
        }
        return new ResponseEntity<>(numberOfAccounts + " Transactions Created", HttpStatus.CREATED);
    }

    @DeleteMapping("TEST/batch/deleteTransaction")
    public ResponseEntity<String> deleteAccounts(@RequestBody List<UUID> accountsList) {
        for (UUID account : accountsList) {
            transactionService.deleteTransaction(account);
        }
        return new ResponseEntity<>(accountsList.size() + " accounts have been deleted", HttpStatus.OK);
    }
}
