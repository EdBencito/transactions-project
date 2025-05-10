package dev.ed.transaction_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "transactions", schema = "transaction_service_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Transaction {
//    @ManyToOne
//    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
//    private Account account;

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


    public enum MerchantCategory {
        GAMBLING,
        ADULT_SERVICES,
        CRYPTOCURRENCY,
        FOOD_AND_DINING,
        ELECTRONICS_AND_TECHNOLOGY,
        HOME_AND_GARDEN,
        APPAREL_AND_ACCESSORIES,
        AUTOMOTIVE,
        ENTERTAINMENT,
        ONLINE_PURCHASES,
        HEALTH_AND_WELLNESS,
        TRAVEL,
        UTILITIES;

        public static MerchantCategory getRandomCategory() {
            MerchantCategory[] values = MerchantCategory.values();
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }

        public static MerchantCategory getRandomFraudulentCategory() {
            MerchantCategory[] values = {GAMBLING, ADULT_SERVICES, CRYPTOCURRENCY};
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

    public enum TransactionType {
        DEBIT,
        CREDIT,
        TRANSFER,
        PURCHASE,
        WITHDRAWAL,
        DEPOSIT,
        PAYMENT;

        public static TransactionType getRandomType() {
            TransactionType[] values = TransactionType.values();
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

    public enum TransactionChannel {
        WEB,
        MOBILE_APP,
        ATM,
        POS,
        PHONE_BANKING,
        BRANCH;

        public static TransactionChannel getRandomChannel() {
            TransactionChannel[] values = TransactionChannel.values();
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }

        public static TransactionChannel getRandomFraudulentChannel() {
            TransactionChannel[] values = {MOBILE_APP, WEB};
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

    public enum TransactionStatus {
        PENDING,
        APPROVED,
        DECLINED,
        FLAGGED,
        REVERSED,
        CANCELLED,
        EXPIRED;

        public static TransactionStatus getRandomStatus() {
            TransactionStatus[] values = {PENDING, APPROVED, DECLINED, REVERSED, CANCELLED, EXPIRED};
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

}