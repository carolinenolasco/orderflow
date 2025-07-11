package com.orderflow.order_receiver_service.client;

import com.orderflow.order_receiver_service.dto.PedidoRequestDTO;
import com.orderflow.order_receiver_service.dto.PedidoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service", url = "http://localhost:8084")
public interface OrderClient {

    @PostMapping("/pedidos")
    PedidoResponseDTO criarPedido(@RequestBody PedidoRequestDTO dto);

    @PutMapping("/pedidos/{id}/status")
    void atualizarStatus(@PathVariable Long id, @RequestParam String status);
}

