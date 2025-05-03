package dev.ed.account_service.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositWithdrawDTO {
    @NotBlank(message = "accountId is required")
    private UUID accountId;

    @DecimalMin("0.0")
    private BigDecimal amount;
}
