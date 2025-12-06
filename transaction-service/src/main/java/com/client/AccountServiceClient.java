package com.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @PutMapping("/accounts/{accountNumber}/balance")
    void updateBalance(@PathVariable("accountNumber") String accountNumber,
                       @RequestParam("amount") double amount);
}
