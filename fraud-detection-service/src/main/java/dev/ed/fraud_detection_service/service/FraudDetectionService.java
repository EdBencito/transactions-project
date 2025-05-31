package dev.ed.fraud_detection_service.service;

import dev.ed.avro.TransactionInitiatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Transactional
@Service
public class FraudDetectionService {

    @KafkaListener(topics = "${app.kafka.topic.transaction-service}", groupId = "fraud-detection")
    public void handleInitiatedEvent(TransactionInitiatedEvent event) {

        System.out.println("Received" + "\n" +
                "Transaction: " + event.getTransactionId() +
                "from Account: " + event.getTransactionId() +
                "at: " + Instant.now());

        //TODO: Optional: logs maybe?

        flagTransaction(event);


        //TODO: business logic
    }

    private void flagTransaction(TransactionInitiatedEvent event) {
        // TODO: Communicate with AccountService to update balance

        System.out.println("Transaction Flagged for: " + "\n" +
                " Transaction: " + event.getTransactionId() +
                "from Account: " + event.getTransactionId() +
                "at: " + Instant.now());

    }

}
