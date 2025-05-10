package dev.ed.transaction_service.DTOs;

import dev.ed.transaction_service.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailsResponseDTO {
    private UUID transactionId;
    private UUID accountId;
    private Transaction.TransactionStatus transactionStatus;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdated;
    private Transaction.TransactionType transactionType;
    private String currency;
    private BigDecimal amount;
    private Transaction.MerchantCategory merchantCategory;
    private Transaction.TransactionChannel transactionChannel;
    private boolean isFraudulent;
}
