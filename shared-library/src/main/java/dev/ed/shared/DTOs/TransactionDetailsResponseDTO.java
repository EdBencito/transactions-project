package dev.ed.shared.DTOs;

import dev.ed.shared.enums.MerchantCategory;
import dev.ed.shared.enums.TransactionChannel;
import dev.ed.shared.enums.TransactionStatus;
import dev.ed.shared.enums.TransactionType;
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
    private TransactionStatus transactionStatus;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdated;
    private TransactionType transactionType;
    private String currency;
    private BigDecimal amount;
    private MerchantCategory merchantCategory;
    private TransactionChannel transactionChannel;
    private boolean isFraudulent;
}
