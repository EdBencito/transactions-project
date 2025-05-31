package dev.ed.shared.DTOs;

import dev.ed.shared.enums.TransactionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailsUpdateDTO {
    @NotBlank(message = "transactionId is required")
    private UUID transactionId;
    @NotNull(message = "transactionStatus is required")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;;
}
