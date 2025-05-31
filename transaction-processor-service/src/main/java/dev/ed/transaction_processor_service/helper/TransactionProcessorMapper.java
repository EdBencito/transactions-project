package dev.ed.transaction_processor_service.helper;

import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.avro.TransactionProcessedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TransactionProcessorMapper {

    public TransactionProcessedEvent toTransactionProcessedEvent(TransactionInitiatedEvent transactionInitiatedEvent) {
        return TransactionProcessedEvent.newBuilder()
                .setTransactionId(transactionInitiatedEvent.getTransactionId())
                .setAccountId(transactionInitiatedEvent.getAccountId())
                .setTransactionStatus(transactionInitiatedEvent.getTransactionStatus())
                .setAmount(transactionInitiatedEvent.getAmount())
                .setProcessedAt(toEpochMilliseconds(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())))
                .build();
    }

    private Instant toEpochMilliseconds(LocalDateTime timestamp) {
        return timestamp.atZone(ZoneId.systemDefault()).toInstant();
    }
}
