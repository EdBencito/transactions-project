package dev.ed.fraud_detection_service.helper;

import dev.ed.avro.TransactionFlaggedEvent;
import dev.ed.avro.TransactionInitiatedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class FraudDetectionMapper {

    public TransactionFlaggedEvent toTransactionFlaggedEvent(TransactionInitiatedEvent transactionInitiatedEvent) {
        return TransactionFlaggedEvent.newBuilder()
                .setTransactionId(transactionInitiatedEvent.getTransactionId())
                .setAccountId(transactionInitiatedEvent.getAccountId())
                .setAmount(transactionInitiatedEvent.getAmount())
                .setFlaggedAt(toEpochMilliseconds(LocalDateTime.from(Instant.now())))
                .setIsFlagged(transactionInitiatedEvent.getIsFlagged())
                .build();
    }

    private Instant toEpochMilliseconds(LocalDateTime timestamp) {
        return timestamp.atZone(ZoneId.systemDefault()).toInstant();
    }
}
