package dev.ed.transaction_processor_service.client;

import dev.ed.shared.DTOs.AccountDetailsResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "account-service", url = "http://localhost:8081")
public interface AccountClient {
    @GetMapping("/accounts/{accountId}")
     Optional<AccountDetailsResponseDTO> getAccountDetails(@PathVariable UUID accountId);

    }
