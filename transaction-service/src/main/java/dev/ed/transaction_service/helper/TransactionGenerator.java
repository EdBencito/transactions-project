package dev.ed.transaction_service.helper;


import dev.ed.transaction_service.client.AccountClient;
import dev.ed.transaction_service.model.Transaction;
import dev.ed.transaction_service.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
@Transactional
@Service
@RequiredArgsConstructor
public class TransactionGenerator {

    private final AccountClient accountClient;
    private final TransactionRepository transactionRepository;

    @SneakyThrows
    public void generateTransactions() {
        Transaction transaction = Transaction.builder()
                .transactionId(generateUUID())
                .accountId(accountClient.getRandomAccountId().orElseThrow(() -> new EntityNotFoundException("No Accounts Found")))
                .transactionStatus(Transaction.TransactionStatus.getRandomStatus())
                .creationDateTime(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .lastUpdated(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .transactionType(Transaction.TransactionType.getRandomType())
                .amount(generateAmountBasedOnProfile(getRandomProfile()))
                .currency("GBP")
                .merchantCategory(Transaction.MerchantCategory.getRandomCategory())
                .transactionChannel(Transaction.TransactionChannel.getRandomChannel())
                .isFraudulent(false)
                .build();
        transactionRepository.save(transaction);
    }

    @SneakyThrows
    public void injectFraudulentTransaction() {

        Transaction transaction = Transaction.builder()
                .transactionId(generateUUID())
                .accountId(accountClient.getRandomAccountId().orElseThrow(() -> new EntityNotFoundException("No Accounts Found")))
                .transactionStatus(Transaction.TransactionStatus.getRandomStatus())
                .creationDateTime(LocalDateTime.from(Instant.now()))
                .lastUpdated(LocalDateTime.from(Instant.now()))
                .transactionType(Transaction.TransactionType.PURCHASE)
                .amount(generateAmountBasedOnProfile("HIGH_SPENDER"))
                .currency("GBP")
                .merchantCategory(Transaction.MerchantCategory.getRandomFraudulentCategory())
                .transactionChannel(Transaction.TransactionChannel.getRandomFraudulentChannel())
                .isFraudulent(true)
                .build();
        transactionRepository.save(transaction);
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
            return BigDecimal.valueOf(100 + ThreadLocalRandom.current().nextDouble() * 500); // Higher amounts
        } else if ("MEDIUM_SPENDER".equals(profile)) {
            return BigDecimal.valueOf(50 + ThreadLocalRandom.current().nextDouble() * 100); // Medium amounts
        } else { // "LOW_SPENDER" or default
            return BigDecimal.valueOf(10 + ThreadLocalRandom.current().nextDouble() * 10); // Lower amounts
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

}
