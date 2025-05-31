package dev.ed.transaction_service.DTOs;

import dev.ed.shared.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFiltersDTO {
    private UUID accountId;
    private TransactionStatus transactionStatus;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdated;
}
