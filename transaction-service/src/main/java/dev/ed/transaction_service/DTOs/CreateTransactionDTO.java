package dev.ed.transaction_service.DTOs;

import dev.ed.shared.enums.MerchantCategory;
import dev.ed.shared.enums.TransactionChannel;
import dev.ed.shared.enums.TransactionType;
import dev.ed.transaction_service.model.Transaction;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {
    private UUID accountId;
    @DecimalMin("0.0")
    private BigDecimal amount;
    private String currency;
    private TransactionType transactionType;
    private MerchantCategory merchantCategory;
    private TransactionChannel transactionChannel;
}
