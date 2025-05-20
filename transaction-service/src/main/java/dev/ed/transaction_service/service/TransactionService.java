package dev.ed.transaction_service.service;

import dev.ed.avro.TransactionInitiatedEvent;
import dev.ed.transaction_service.helper.TransactionMapper;
import dev.ed.transaction_service.model.Transaction;
import dev.ed.transaction_service.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final KafkaTemplate<String, TransactionInitiatedEvent> kafkaTemplate;
    @Value("${app.kafka.topic.transaction-service}")
    private String transactionServiceTopic;

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
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + transactionId));
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
}
