package com.orderflow.order_receiver_service.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoResponseDTO {

    private Long id;
    private Long clienteId;
    private LocalDateTime dataPedido;
    private String status;
    private List<ItemPedidoDTO> itens;
}

