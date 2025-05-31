package dev.ed.transaction_processor_service.service;

import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.avro.TransactionProcessedEvent;
import dev.ed.transaction_processor_service.helper.TransactionProcessorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Transactional
@RequiredArgsConstructor
@Service
public class TransactionProcessorService {

    private final TransactionProcessorMapper transactionProcessorMapper;
    private final KafkaTemplate<String, TransactionProcessedEvent> kafkaTemplate;
    @Value("${app.kafka.topic.transaction-processor-service}")
    private String transactionProcessorServiceTopic;

    @KafkaListener(topics = "${app.kafka.topic.transaction-service}", groupId = "transaction-processor")
    public void handleInitiatedEvent(TransactionInitiatedEvent event) {

        System.out.println("Received" + "\n" +
                "Transaction: " + event.getTransactionId() +
                "from Account: " + event.getTransactionId() +
                "at: " + Instant.now());

        //TODO: Optional: logs maybe?

        updateBalance(event);


        //TODO: business logic

        publish(transactionProcessorMapper.toTransactionProcessedEvent(event));

    }

    private void updateBalance(TransactionInitiatedEvent event) {
        // TODO: Communicate with AccountService to update balance

        System.out.println("Balance Updated for: " + "\n" +
                " Transaction: " + event.getTransactionId() +
                "from Account: " + event.getTransactionId() +
                "at: " + Instant.now());

    }

    public void publish(TransactionProcessedEvent event) {
        kafkaTemplate.send(transactionProcessorServiceTopic, event.getTransactionId(), event);
    }

}
