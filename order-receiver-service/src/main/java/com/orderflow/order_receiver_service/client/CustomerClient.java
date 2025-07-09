package com.orderflow.order_receiver_service.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CustomerClient {

    @GetMapping("/clientes/{id}")
    Object buscarPorId(@PathVariable Long id); // não importa o retorno, só verificamos se existe
}
