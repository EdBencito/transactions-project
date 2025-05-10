package dev.ed.transaction_service.service;

import dev.ed.transaction_service.model.Transaction;
import dev.ed.transaction_service.repository.TransactionRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + transactionId));
    }

    public List<Transaction> getAllTransactionsByFilters(@Nullable UUID accountId, @Nullable Transaction.TransactionStatus transactionStatus,
                                                         @Nullable LocalDateTime creationDateTime, @Nullable LocalDateTime lastUpdated) {
        return transactionRepository.findByFilters(accountId, transactionStatus, creationDateTime, lastUpdated);
    }

    public List<Transaction> getAllTransactionsByAccountId(UUID accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }

    public void deleteTransaction(UUID transactionId) {
        transactionRepository.deleteById(transactionId);
    }

}
