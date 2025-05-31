package dev.ed.account_service.client;


import dev.ed.shared.DTOs.TransactionDetailsUpdateDTO;
import dev.ed.shared.DTOs.TransactionDetailsResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "transaction-service", url = "http://localhost:8082")
public interface TransactionClient {
    @GetMapping("/transaction/{transactionId}")
    Optional<TransactionDetailsResponseDTO> getTransaction(@PathVariable UUID transactionId);

    @PatchMapping("/transaction/{transactionId}/transactionDetails")
    Optional<TransactionDetailsResponseDTO> updateTransactionDetails(@RequestBody TransactionDetailsUpdateDTO detailsUpdateRequest);
}


