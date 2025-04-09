package dev.ed.account_service.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Account {

    @NotBlank(message = "accountId is required")
    private UUID accountId;

    @NotBlank(message = "accountNumber is required")
    private String accountNumber;

    @NotBlank(message = "accountType is required")
    private AccountType accountType;

    @NotBlank(message = "accountStatus is required")
    private AccountStatus accountStatus;

    @NotBlank(message = "accountHolderName is required")
    private String accountHolderName;

    @NotBlank(message = "openingDate is required")
    private LocalDate openingDate;

    @NotBlank(message = "currency is required")
    private String currency;

    @NotBlank(message = "balance is required")
    private BigDecimal balance;

    @NotBlank(message = "interestRate is required")
    private BigDecimal interestRate;

    @NotBlank(message = "creditLimit is required")
    private BigDecimal creditLimit;

    public enum AccountType {
        SAVINGS,
        CHECKING,
        CREDIT_CARD,
        LOAN,
        INVESTMENT;

        public static AccountType getRandomAccountType() {
            AccountType[] values = AccountType.values();
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        PENDING,
        CLOSED,
        FROZEN;
    }
}
