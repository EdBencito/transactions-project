package dev.ed.transaction_service.model;

import dev.ed.shared.enums.MerchantCategory;
import dev.ed.shared.enums.TransactionChannel;
import dev.ed.shared.enums.TransactionStatus;
import dev.ed.shared.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions", schema = "transaction_service_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false, unique = true)
    private UUID transactionId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "transaction_status", nullable = false)
    @NotNull(message = "transactionStatus is required")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "creation_date_time", nullable = false)
    @NotNull(message = "creationDateTime is required")
    private LocalDateTime creationDateTime;

    @Column(name = "last_updated", nullable = false)
    @NotNull(message = "lastUpdated is required")
    private LocalDateTime lastUpdated;

    @Column(name = "transaction_type", nullable = false)
    @NotNull(message = "transactionType is required")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "amount is required")
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    @NotBlank(message = "currency is required")
    private String currency;

    @Column(name = "merchant_category", nullable = false)
    @NotNull(message = "merchantCategory is required")
    @Enumerated(EnumType.STRING)
    private MerchantCategory merchantCategory;

    @Column(name = "transaction_channel", nullable = false)
    @NotNull(message = "transactionChannel is required")
    @Enumerated(EnumType.STRING)
    private TransactionChannel transactionChannel;

    @Column(name = "is_fraudulent", nullable = false)
    private boolean isFraudulent;
}