package dev.ed.transaction_processor_service.helper;

import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.avro.TransactionProcessedEvent;
import dev.ed.shared.enums.TransactionStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TransactionProcessorMapper {

    public TransactionProcessedEvent toTransactionProcessedEvent(TransactionInitiatedEvent transactionInitiatedEvent, TransactionStatus transactionStatus) {
        return TransactionProcessedEvent.newBuilder()
                .setTransactionId(transactionInitiatedEvent.getTransactionId())
                .setAccountId(transactionInitiatedEvent.getAccountId())
                .setTransactionStatus(mapToAvroTransactionStatus(transactionStatus))
                .setAmount(transactionInitiatedEvent.getAmount())
                .setProcessedAt(toEpochMilliseconds(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())))
                .build();
    }

    private Instant toEpochMilliseconds(LocalDateTime timestamp) {
        return timestamp.atZone(ZoneId.systemDefault()).toInstant();
    }

    public dev.ed.avro.TransactionStatus mapToAvroTransactionStatus(TransactionStatus status) {
        return dev.ed.avro.TransactionStatus.valueOf(status.name());
    }
}
