package dev.ed.shared.DTOs;

import dev.ed.shared.enums.AccountStatus;
import dev.ed.shared.enums.AccountType;
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
    private AccountType accountType;
    private AccountStatus accountStatus;
    private String accountHolderName;
    private String currency;
    private BigDecimal balance;
    private BigDecimal interestRate;
    private BigDecimal creditLimit;
}
