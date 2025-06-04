package dev.ed.fraud_detection_service.helper;

import dev.ed.avro.TransactionFlaggedEvent;
import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.shared.enums.TransactionStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class FraudDetectionMapper {

    public TransactionFlaggedEvent toTransactionFlaggedEvent(TransactionInitiatedEvent transactionInitiatedEvent, TransactionStatus transactionStatus) {
        return TransactionFlaggedEvent.newBuilder()
                .setTransactionId(transactionInitiatedEvent.getTransactionId())
                .setAccountId(transactionInitiatedEvent.getAccountId())
                .setTransactionStatus(mapToAvroTransactionStatus(transactionStatus))
                .setAmount(transactionInitiatedEvent.getAmount())
                .setFlaggedAt(toEpochMilliseconds(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())))
                .setIsFlagged(transactionInitiatedEvent.getIsFlagged())
                .build();
    }


    private Instant toEpochMilliseconds(LocalDateTime timestamp) {
        return timestamp.atZone(ZoneId.systemDefault()).toInstant();
    }

    public dev.ed.avro.TransactionStatus mapToAvroTransactionStatus(TransactionStatus status) {
        return dev.ed.avro.TransactionStatus.valueOf(status.name());
    }
}
