package dev.ed.transaction_processor_service.service;

import dev.ed.avro.TransactionInitiatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Transactional
@Service
public class TransactionProcessorService {

    @KafkaListener(topics = "transactions", groupId = "transaction-processor")
    public void process(TransactionInitiatedEvent event) {

        System.out.println("Received" + "\n" +
                "Transaction: " + event.getTransactionId() +
                "from Account: " + event.getTransactionId() +
                "at: " + Instant.now());

        //TODO: Optional: logs maybe?

        updateBalance(event);


        //TODO: business logic
    }

    private void updateBalance(TransactionInitiatedEvent event) {
        // TODO: Communicate with AccountService to update balance

        System.out.println("Balance Updated for: " + "\n" +
                " Transaction: " + event.getTransactionId() +
                "from Account: " + event.getTransactionId() +
                "at: " + Instant.now());

    }

}
