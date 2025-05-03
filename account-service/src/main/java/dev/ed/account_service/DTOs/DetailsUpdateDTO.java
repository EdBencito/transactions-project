package dev.ed.account_service.DTOs;

import dev.ed.account_service.model.Account;
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
public class DetailsUpdateDTO {
    @NotBlank(message = "accountId is required")
    private UUID accountId;
    private Account.AccountStatus accountStatus;
    private String currency;
    @DecimalMin("0.0")
    private BigDecimal interestRate;
    @DecimalMin("0.0")
    private BigDecimal creditLimit;
}
