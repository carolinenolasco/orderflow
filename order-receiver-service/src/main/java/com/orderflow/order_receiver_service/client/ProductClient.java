package com.orderflow.order_receiver_service.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductClient {

    @GetMapping("/produtos/{id}")
    Object buscarPorId(@PathVariable Long id);
}
