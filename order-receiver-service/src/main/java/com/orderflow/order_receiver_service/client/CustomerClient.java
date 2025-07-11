package com.orderflow.order_receiver_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "http://localhost:8081")
public interface CustomerClient {

    @GetMapping("/clientes/{id}")
    void buscarPorId(@PathVariable Long id);
}

