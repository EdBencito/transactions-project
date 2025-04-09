package dev.ed.account_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ed.account_service.model.Account;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountGenerator {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public String generateAccount(String accountHolderName) {
        Account account = Account.builder()
                .accountId(generateUUID())
                .accountNumber(generateAccountNumber())
                .accountType(Account.AccountType.getRandomAccountType())
                .accountStatus(Account.AccountStatus.ACTIVE)
                .accountHolderName(accountHolderName)
                .openingDate(generateOpeningDate())
                .currency("GBP")
                .balance(generateBalance())
                .interestRate(BigDecimal.valueOf(4.5))
                .creditLimit(BigDecimal.valueOf(4000))
                .build();
        return objectMapper.writeValueAsString(account);
    }

    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    private String generateAccountNumber() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(10_000_000_000L, 100_000_000_000L));
    }

    private LocalDate generateOpeningDate() {
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(
                LocalDate.of(2000, 1, 1).toEpochDay(),
                LocalDate.now().toEpochDay()));
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

    public static BigDecimal generateBalance() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1000.0, 16000.0));
    }
}
