package dev.ed.account_service.model;

import dev.ed.shared.enums.AccountStatus;
import dev.ed.shared.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "Invalid UUID format")
    @NotBlank(message = "accountId is required")
    private UUID accountId;

    @Column(name = "account_number", nullable = false, unique = true)
    @NotBlank(message = "accountNumber is required")
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    @NotNull(message = "accountType is required")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "account_status", nullable = false)
    @NotNull(message = "accountStatus is required")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "account_holder_name", nullable = false)
    @NotBlank(message = "accountHolderName is required")
    private String accountHolderName;

    @Column(name = "opening_date", nullable = false)
    @NotNull(message = "openingDate is required")
    private LocalDate openingDate;

    @Column(name = "currency", nullable = false)
    @NotBlank(message = "currency is required")
    private String currency;

    @Column(name = "balance", nullable = false)
    @NotNull(message = "balance is required")
    private BigDecimal balance;

    @Column(name = "interest_rate", nullable = false)
    @NotNull(message = "interestRate is required")
    private BigDecimal interestRate;

    @Column(name = "credit_limit", nullable = false)
    @NotNull(message = "creditLimit is required")
    private BigDecimal creditLimit;


}
