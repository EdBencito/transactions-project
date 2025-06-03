package dev.ed.transaction_processor_service.service;

import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.avro.TransactionProcessedEvent;
import dev.ed.avro.TransactionType;
import dev.ed.shared.DTOs.AccountDetailsResponseDTO;
import dev.ed.shared.enums.TransactionStatus;
import dev.ed.transaction_processor_service.client.AccountClient;
import dev.ed.transaction_processor_service.helper.TransactionProcessorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class TransactionProcessorService {

    private final TransactionProcessorMapper transactionProcessorMapper;
    private final AccountClient accountClient;
    private final KafkaTemplate<String, TransactionProcessedEvent> kafkaTemplate;
    @Value("${app.kafka.topic.transaction-processor-service}")
    private String transactionProcessorServiceTopic;

    @KafkaListener(topics = "${app.kafka.topic.transaction-service}", groupId = "transaction-processor")
    public void handleInitiatedEvent(TransactionInitiatedEvent event) {

        try {
            System.out.println("Received" + "\n" +
                    "Transaction: " + event.getTransactionId() +
                    "from Account: " + event.getAccountId() +
                    "at: " + Instant.now());

            String accountID = event.getAccountId();
            AccountDetailsResponseDTO accountDetailsResponseDTO = accountClient.getAccountDetails(UUID.fromString(accountID))
                    .orElseThrow(() -> new NoSuchElementException("Account not found with ID: " + accountID));

            TransactionStatus newTransactionStatus = TransactionStatus.PENDING;

            if (event.getTransactionType().equals(TransactionType.DEBIT)) {
                if (accountDetailsResponseDTO.getBalance().compareTo(event.getAmount()) < 0) {
                    newTransactionStatus = TransactionStatus.DECLINED;
                } else {
                    newTransactionStatus = TransactionStatus.APPROVED;
                }
            }

            publish(transactionProcessorMapper.toTransactionProcessedEvent(event, newTransactionStatus));
        } catch (NoSuchElementException e) {
            System.out.println("Error processing transaction" + e.getMessage());
        }

    }

    public void publish(TransactionProcessedEvent event) {
        kafkaTemplate.send(transactionProcessorServiceTopic, event.getTransactionId(), event);
    }

}
