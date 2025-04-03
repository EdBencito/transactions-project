package dev.ed.transaction_generator_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ed.transaction_generator_service.model.Transaction;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TransactionGenerator {

    private final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization

    @SneakyThrows
    public String generateTransactionEvent(String accountId) {
        Transaction transaction = Transaction.builder()
                .transactionId(generateUUID())
                .accountId(accountId)
                .timestamp(LocalDateTime.from(Instant.now()))
                .transactionType(Transaction.TransactionType.getRandomType())
                .amount(generateAmountBasedOnProfile(getRandomProfile()))
                .currency(getRandomCurrency())
                .merchantCategory(Transaction.MerchantCategory.getRandomCategory())
                .deviceInfo(Transaction.DeviceInfo.getRandomInfo())
                .ipAddress(generateRandomIpAddress())
                .transactionChannel(Transaction.TransactionChannel.getRandomChannel())
                .isFraudulent(false)
                .build();

        return objectMapper.writeValueAsString(transaction);
    }


    @SneakyThrows
    public String injectFraudulentTransaction(String accountId) {

        Transaction transaction = Transaction.builder()
                .transactionId(generateUUID())
                .accountId(accountId)
                .timestamp(LocalDateTime.from(Instant.now()))
                .transactionType(Transaction.TransactionType.PURCHASE)
                .amount(generateAmountBasedOnProfile("HIGH_SPENDER"))
                .currency(getRandomCurrency())
                .merchantCategory(Transaction.MerchantCategory.getRandomFraudulentCategory())
                .deviceInfo(Transaction.DeviceInfo.getRandomFraudulentInfo())
                .ipAddress(generateRandomIpAddress())
                .transactionChannel(Transaction.TransactionChannel.getRandomFraudulentChannel())
                .isFraudulent(true)
                .build();

        return objectMapper.writeValueAsString(transaction);
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    private String getRandomProfile() {
        String[] types = {"LOW_SPENDER", "MEDIUM_SPENDER", "HIGH_SPENDER"};
        return types[ThreadLocalRandom.current().nextInt(types.length)];
    }

    private BigDecimal generateAmountBasedOnProfile(String profile) {
        if ("HIGH_SPENDER".equals(profile)) {
            return BigDecimal.valueOf(100 + ThreadLocalRandom.current().nextDouble() * 1000); // Higher amounts
        } else if ("MEDIUM_SPENDER".equals(profile)) {
            return BigDecimal.valueOf(50 + ThreadLocalRandom.current().nextDouble() * 1000); // Medium amounts
        } else { // "LOW_SPENDER" or default
            return BigDecimal.valueOf(10 + ThreadLocalRandom.current().nextDouble() * 1000); // Lower amounts
        }
    }

    public static String getRandomCurrency() {
        Set<Currency> currencyCodes = new HashSet<>();
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            try {
                Currency currency = Currency.getInstance(locale);

                if (currency != null) {
                    currencyCodes.add(currency);
                }
            } catch (Exception exc) {
                // Locale not found
            }
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(currencyCodes.size());

        Iterator<Currency> iterator = currencyCodes.iterator();
        for (int i = 0; i < randomIndex; i++) {
            iterator.next(); // Advance the iterator
        }

        return iterator.next().toString(); // Return the element at the random index.
    }

    private String generateRandomIpAddress() {
        return String.format("%d.%d.%d.%d",
                ThreadLocalRandom.current().nextInt(256),
                ThreadLocalRandom.current().nextInt(256),
                ThreadLocalRandom.current().nextInt(256),
                ThreadLocalRandom.current().nextInt(256));
    }
}
