package dev.ed.fraud_detection_service.service;

import dev.ed.avro.TransactionFlaggedEvent;
import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.fraud_detection_service.helper.FraudDetectionMapper;
import dev.ed.shared.enums.TransactionStatus;
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
public class FraudDetectionService {

    private final FraudDetectionMapper fraudDetectionMapper;
    private final KafkaTemplate<String, TransactionFlaggedEvent> kafkaTemplate;
    @Value("${app.kafka.topic.fraud-detection-service}")
    private String fraudDetectionServiceTopic;

    @KafkaListener(topics = "${app.kafka.topic.transaction-service}", groupId = "fraud-detection")
    public void handleInitiatedEvent(TransactionInitiatedEvent event) {
        try {
            System.out.println("Received" + "\n" +
                    "Transaction: " + event.getTransactionId() +
                    "from Account: " + event.getAccountId() +
                    "at: " + Instant.now());

            if (event.getIsFlagged()) {
                flagTransaction(event);
            } else {
                System.out.println("Transaction [{}] not flagged. Skipping fraud action. " + event.getTransactionId());
            }
        } catch (Exception e) {
            System.out.println("Error flagging transaction" + e.getMessage());
        }
    }

    private void flagTransaction(TransactionInitiatedEvent event) {
        TransactionStatus newTransactionStatus = TransactionStatus.FLAGGED;
        publish(fraudDetectionMapper.toTransactionFlaggedEvent(event, newTransactionStatus));

        System.out.println("Transaction Flagged for: " + "\n" +
                " Transaction: " + event.getTransactionId() +
                "from Account: " + event.getAccountId() +
                "at: " + Instant.now());

    }

    public void publish(TransactionFlaggedEvent event) {
        kafkaTemplate.send(fraudDetectionServiceTopic, event.getTransactionId(), event);
    }
}
