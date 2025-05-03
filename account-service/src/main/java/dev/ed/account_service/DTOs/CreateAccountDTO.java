package dev.ed.account_service.DTOs;

import dev.ed.account_service.model.Account;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDTO {

    private String accountHolderName;
    private Account.AccountType accountType;
    private String currency;

    @DecimalMin("0.0")
    private BigDecimal balance; //optional
    @DecimalMin("0.0")
    private BigDecimal interestRate; //optional
    @DecimalMin("0.0")
    private BigDecimal creditLimit; //optional
}
