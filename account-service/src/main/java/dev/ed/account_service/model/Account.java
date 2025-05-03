package dev.ed.account_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "accounts", schema = "account_service_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Account {

    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    @NotBlank(message = "accountId is required")
    private UUID accountId;

    @Column(name = "account_number", nullable = false, unique = true)
    @NotBlank(message = "accountNumber is required")
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    @NotBlank(message = "accountType is required")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "account_status", nullable = false)
    @NotBlank(message = "accountStatus is required")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "account_holder_name", nullable = false)
    @NotBlank(message = "accountHolderName is required")
    private String accountHolderName;

    @Column(name = "opening_date", nullable = false)
    @NotBlank(message = "openingDate is required")
    private LocalDate openingDate;

    @Column(name = "currency", nullable = false)
    @NotBlank(message = "currency is required")
    private String currency;

    @Column(name = "balance", nullable = false)
    @NotBlank(message = "balance is required")
    private BigDecimal balance;

    @Column(name = "interest_rate", nullable = false)
    @NotBlank(message = "interestRate is required")
    private BigDecimal interestRate;

    @Column(name = "credit_limit", nullable = false)
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
