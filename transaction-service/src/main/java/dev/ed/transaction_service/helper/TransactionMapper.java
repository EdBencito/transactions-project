package dev.ed.transaction_service.helper;

import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.transaction_service.DTOs.CreateTransactionDTO;
import dev.ed.transaction_service.DTOs.TransactionDetailsResponseDTO;
import dev.ed.transaction_service.exception.MaxRetriesException;
import dev.ed.transaction_service.model.Transaction;
import dev.ed.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Conversions;
import org.apache.avro.LogicalTypes;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
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
                .amount(dto.getAmount()) //TODO: fix the decimal point issue: when using a requestbody 10.00  would be saved as 10 in the database.
                .currency("GBP")
                .merchantCategory(dto.getMerchantCategory())
                .transactionChannel(dto.getTransactionChannel())
                .isFraudulent(false)
                .build();
    }

    public TransactionInitiatedEvent toTransactionInitiatedEvent(Transaction transaction) {
         return TransactionInitiatedEvent.newBuilder()
                 .setTransactionId(String.valueOf(transaction.getTransactionId()))
                 .setAccountId(String.valueOf(transaction.getAccountId()))
                 .setAmount(transaction.getAmount())
                 .setTimestamp(toEpochMilliseconds(transaction.getCreationDateTime()))
                 .setTransactionDate(transaction.getCreationDateTime().toLocalDate())
                 .build();
    }

    private UUID generateUUID() {
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




    private Instant toEpochMilliseconds(LocalDateTime timestamp){
        return timestamp.atZone(ZoneId.systemDefault()).toInstant();
    }


    public static LocalDateTime toLocalDateTime(long timestampMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), ZoneId.systemDefault());
    }


}
