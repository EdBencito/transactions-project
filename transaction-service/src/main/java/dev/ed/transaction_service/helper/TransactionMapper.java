package dev.ed.transaction_service.helper;

import dev.ed.transaction_service.DTOs.CreateTransactionDTO;
import dev.ed.transaction_service.DTOs.TransactionDetailsResponseDTO;
import dev.ed.transaction_service.exception.MaxRetriesException;
import dev.ed.transaction_service.model.Transaction;
import dev.ed.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TransactionMapper {
    private final TransactionRepository transactionRepository;

    public TransactionDetailsResponseDTO toResponseDTO(Transaction transaction) {
        return TransactionDetailsResponseDTO.builder()
                .transactionId(transaction.getTransactionId())
                .accountId(transaction.getAccountId())
                .transactionStatus(transaction.getTransactionStatus())
                .creationDate(transaction.getCreationDateTime())
                .lastUpdated(transaction.getLastUpdated())
                .transactionType(transaction.getTransactionType())
                .currency(transaction.getCurrency())
                .amount(transaction.getAmount())
                .merchantCategory(transaction.getMerchantCategory())
                .transactionChannel(transaction.getTransactionChannel())
                .isFraudulent(transaction.isFraudulent())
                .build();
    }

    public Transaction toTransactionEntity(CreateTransactionDTO dto) {
        return Transaction.builder()
                .transactionId(generateUUID())
                .accountId(dto.getAccountId())
                .transactionStatus(Transaction.TransactionStatus.PENDING)
                .creationDateTime(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .lastUpdated(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .transactionType(dto.getTransactionType())
                .amount(dto.getAmount())
                .currency("GBP")
                .merchantCategory(dto.getMerchantCategory())
                .transactionChannel(dto.getTransactionChannel())
                .isFraudulent(false)
                .build();
    }

    public UUID generateUUID() {
        int maxRetries = 5;
        int retryCount = 0;
        UUID accountId;
        do {
            accountId = UUID.randomUUID();
            retryCount++;
        } while (transactionRepository.existsById(accountId) && retryCount < maxRetries);
        if (retryCount == maxRetries)
            throw new MaxRetriesException("Max Retries for accountId generation has been hit");// Retry if exists
        return accountId;
    }
}
