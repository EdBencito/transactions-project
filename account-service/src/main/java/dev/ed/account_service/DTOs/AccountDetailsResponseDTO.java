package dev.ed.account_service.DTOs;

import dev.ed.account_service.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsResponseDTO {
    private String accountNumber;
    private Account.AccountType accountType;
    private Account.AccountStatus accountStatus;
    private String accountHolderName;
    private String currency;
    private BigDecimal balance;
    private BigDecimal interestRate;
    private BigDecimal creditLimit;
}
