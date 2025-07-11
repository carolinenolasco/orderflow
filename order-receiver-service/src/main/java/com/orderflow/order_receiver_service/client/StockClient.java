package com.orderflow.order_receiver_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stock-service", url = "http://localhost:8083")
public interface StockClient {

    @GetMapping("/estoques/produto/{productId}")
    EstoqueResponse buscarPorProductId(@PathVariable Long productId);

    record EstoqueResponse(Long id, Long productId, Integer quantidade) {}
}

