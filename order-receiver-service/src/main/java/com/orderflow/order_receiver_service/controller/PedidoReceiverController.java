package com.orderflow.order_receiver_service.controller;

import com.orderflow.order_receiver_service.dto.PedidoReceiverRequestDTO;
import com.orderflow.order_receiver_service.dto.PedidoResponseDTO;
import com.orderflow.order_receiver_service.service.PedidoReceiverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class PedidoReceiverController {

    @Autowired
    private PedidoReceiverService service;

    @PostMapping
    public PedidoResponseDTO receberPedido(@Valid @RequestBody PedidoReceiverRequestDTO dto) {
        return service.processarPedido(dto);
    }
}
