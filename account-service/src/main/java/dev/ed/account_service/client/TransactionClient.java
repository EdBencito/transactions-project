package dev.ed.account_service.client;


import dev.ed.shared.DTOs.TransactionDetailsUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "transaction-service", url = "http://localhost:8082")
public interface TransactionClient {

    @PutMapping("/transaction/transactionDetails")
    void updateTransactionDetails(@RequestBody TransactionDetailsUpdateDTO detailsUpdateRequest);
}


