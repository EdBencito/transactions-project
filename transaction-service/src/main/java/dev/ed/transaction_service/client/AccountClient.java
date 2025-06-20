package dev.ed.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "account-service", url = "http://localhost:8081")
public interface AccountClient {
    @GetMapping("/internal/accounts/randomAccountId")
    Optional<UUID> getRandomAccountId();
}
