package dev.ed.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "account-service", url = "http://localhost:8080")
public interface AccountClient {
    @GetMapping("/accounts/TEST/randomAccountId")
    Optional<UUID> getRandomAccountId();
}
