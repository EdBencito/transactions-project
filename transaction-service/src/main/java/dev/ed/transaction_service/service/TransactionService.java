package dev.ed.transaction_service.service;

import dev.ed.avro.BalanceUpdateEvent;
import dev.ed.avro.TransactionFlaggedEvent;
import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.avro.TransactionProcessedEvent;
import dev.ed.shared.DTOs.TransactionDetailsUpdateDTO;
import dev.ed.shared.enums.TransactionStatus;
import dev.ed.transaction_service.helper.TransactionMapper;
import dev.ed.transaction_service.model.Transaction;
import dev.ed.transaction_service.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final KafkaTemplate<String, TransactionInitiatedEvent> kafkaTemplate;
    private final KafkaTemplate<String, BalanceUpdateEvent> kafkaTemplateBalanceUpdate;
    @Value("${app.kafka.topic.transaction-service}")
    private String transactionServiceTopic;
    @Value("${app.kafka.topic.transaction-service-update}")
    private String balanceUpdateTopic;

    public Transaction createTransaction(Transaction transaction) {
        Transaction saved = transactionRepository.save(transaction);
        publish(transactionMapper.toTransactionInitiatedEvent(saved));
        System.out.println("Published" + "\n" +
                "Transaction: " + transaction.getTransactionId() +
                "from Account: " + transaction.getTransactionId() +
                "at: " + Instant.now());
        return saved;
    }

    public Transaction getTransaction(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + transactionId));
    }

    public Transaction updateTransactionDetails(UUID transactionId, TransactionDetailsUpdateDTO newDetails) {
        Transaction transaction = getTransaction(transactionId);
        transaction.setTransactionStatus(newDetails.getTransactionStatus() != null ? newDetails.getTransactionStatus() : transaction.getTransactionStatus());
        transaction.setLastUpdated(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactionsByAccountId(UUID accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }

    public void deleteTransaction(UUID transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public void publish(TransactionInitiatedEvent event) {
        kafkaTemplate.send(transactionServiceTopic, event.getTransactionId(), event);
    }

    public void publish(BalanceUpdateEvent event) {
        kafkaTemplateBalanceUpdate.send(balanceUpdateTopic, event.getTransactionId(), event);
    }

    @KafkaListener(topics = "${app.kafka.topic.transaction-processor-service}", groupId = "transaction-service", containerFactory = "transactionProcessedEventConcurrentKafkaListenerContainerFactory")
    public void handleProcessedEvent(TransactionProcessedEvent event) {
        TransactionDetailsUpdateDTO newDetails = new TransactionDetailsUpdateDTO();
        newDetails.setTransactionId(UUID.fromString(event.getTransactionId()));
        newDetails.setTransactionStatus(TransactionStatus.APPROVED);
        Transaction transaction = getTransaction(UUID.fromString(event.getTransactionId()));
        updateTransactionDetails(UUID.fromString(event.getTransactionId()), newDetails);
        publish(transactionMapper.toBalanceUpdateEvent(transaction, event));
    }

    @KafkaListener(topics = "${app.kafka.topic.fraud-detection-service}", groupId = "transaction-service", containerFactory = "transactionFlaggedEventConcurrentKafkaListenerContainerFactory")
    public void handleFlaggedEvent(TransactionFlaggedEvent event) {
        Transaction transaction = getTransaction(UUID.fromString(event.getTransactionId()));
        publish(transactionMapper.toBalanceUpdateEvent(transaction, event));
    }
}
