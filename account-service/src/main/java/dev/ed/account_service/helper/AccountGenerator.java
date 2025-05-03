package dev.ed.account_service.helper;

import dev.ed.account_service.exception.MaxRetriesException;
import dev.ed.account_service.model.Account;
import dev.ed.account_service.repository.AccountRepository;
import lombok.SneakyThrows;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountGenerator {
    private final AccountRepository accountRepository;
    private final static Random random = new Random();

    public AccountGenerator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @SneakyThrows
    public void generateAccount() {
        Account account = Account.builder()
                .accountId(generateUUID())
                .accountNumber(generateAccountNumber())
                .accountType(Account.AccountType.getRandomAccountType())
                .accountStatus(Account.AccountStatus.ACTIVE)
                .accountHolderName(generateRandomName())
                .openingDate(generateOpeningDate())
                .currency(random.nextBoolean() ? "USD" : "GBP")
                .balance(generateBalance())
                .interestRate(BigDecimal.valueOf(4.5))
                .creditLimit(BigDecimal.valueOf(4000))
                .build();
        accountRepository.save(account);
    }

    public String generateAccountNumber() {
        int maxRetries = 5;
        int retryCount = 0;
        String accountNumber;
        do {
            accountNumber = String.valueOf(ThreadLocalRandom.current().nextLong(10_000_000_000L, 100_000_000_000L));
            retryCount++;
        } while (accountRepository.existsByAccountNumber(accountNumber) && retryCount < maxRetries); // Retry if exists
        if (retryCount == maxRetries)
            throw new MaxRetriesException("Max Retries for accountNumber generation has been hit");
        return accountNumber;
    }

    public UUID generateUUID() {
        int maxRetries = 5;
        int retryCount = 0;
        UUID accountId;
        do {
            accountId = UUID.randomUUID();
            retryCount++;
        } while (accountRepository.existsById(accountId) && retryCount < maxRetries);
        if (retryCount == maxRetries)
            throw new MaxRetriesException("Max Retries for accountId generation has been hit");// Retry if exists
        return accountId;
    }

    private LocalDate generateOpeningDate() {
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(
                LocalDate.of(2000, 1, 1).toEpochDay(),
                LocalDate.now().toEpochDay()));
    }

    private static String getRandomCurrency() {
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

    private static BigDecimal generateBalance() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1000.0, 16000.0));
    }

    private String generateRandomName() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }
}
