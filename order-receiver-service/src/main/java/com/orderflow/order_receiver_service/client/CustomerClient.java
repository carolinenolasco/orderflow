package com.orderflow.order_receiver_service.client;

import com.orderflow.order_receiver_service.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${services.customer.url:http://localhost:8081}")
public interface CustomerClient {

    @GetMapping("/clientes/{id}")
    ClienteDTO buscarPorId(@PathVariable Long id);
}

