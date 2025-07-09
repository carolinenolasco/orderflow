package com.orderflow.order_receiver_service.client;

import com.orderflow.order_receiver_service.dto.PedidoRequestDTO;
import com.orderflow.order_receiver_service.dto.PedidoResponseDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderClient {

    @PostMapping("/pedidos")
    PedidoResponseDTO criarPedido(@RequestBody PedidoRequestDTO dto);
}
