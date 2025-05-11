package dev.ed.transaction_service.controller;

import dev.ed.transaction_service.DTOs.CreateTransactionDTO;
import dev.ed.transaction_service.DTOs.TransactionDetailsResponseDTO;
import dev.ed.transaction_service.helper.TransactionMapper;
import dev.ed.transaction_service.model.Transaction;
import dev.ed.transaction_service.service.TransactionService;
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

    @GetMapping("/listTransactions/{accountId}")
    public ResponseEntity<List<TransactionDetailsResponseDTO>> getAllTransactionsByAccountId(@PathVariable UUID accountId) {
        List<TransactionDetailsResponseDTO> transactions = transactionService.getAllTransactionsByAccountId(accountId)
                .stream().map(transactionMapper::toResponseDTO).collect(Collectors.toList());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }


}
