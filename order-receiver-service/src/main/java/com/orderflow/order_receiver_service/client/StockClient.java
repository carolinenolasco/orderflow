package com.orderflow.order_receiver_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stock-service", url = "${services.stock.url:http://localhost:8083}")
public interface StockClient {

    @PutMapping("/estoques/produto/{productId}/baixa")
    void baixar(@PathVariable("productId") Long productId, @RequestParam("quantidade") Integer quantidade);

    @PutMapping("/estoques/produto/{productId}/reposicao")
    void repor(@PathVariable("productId") Long productId, @RequestParam("quantidade") Integer quantidade);
}
