package com.orderflow.order_receiver_service.controller;

import com.orderflow.order_receiver_service.dto.PedidoReceiverRequestDTO;
import com.orderflow.order_receiver_service.queue.PedidoQueue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class PedidoReceiverController {

    @Autowired
    private PedidoQueue pedidoQueue;

    @PostMapping
    public String checkout(@Valid @RequestBody PedidoReceiverRequestDTO dto) {
        pedidoQueue.adicionar(dto);
        return "✅ Pedido recebido e adicionado à fila.";
    }
}
