package com.orderflow.order_receiver_service.dto;

import com.orderflow.order_receiver_service.domain.StatusPedido;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PedidoResponseDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime dataPedido;
    private StatusPedido status;  // troque String -> StatusPedido
    private List<ItemPedidoDTO> itens;
}
