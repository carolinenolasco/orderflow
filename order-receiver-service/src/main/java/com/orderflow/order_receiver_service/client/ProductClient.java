package com.orderflow.order_receiver_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "product-service", url = "http://localhost:8082")
public interface ProductClient {

    @GetMapping("/produtos/{id}")
    ProdutoResponse buscarPorId(@PathVariable Long id);

    record ProdutoResponse(Long id, String nome, BigDecimal preco) {}
}
