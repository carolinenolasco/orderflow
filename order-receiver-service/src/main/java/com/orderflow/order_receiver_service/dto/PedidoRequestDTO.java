package com.orderflow.order_receiver_service.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoRequestDTO {

    private Long clienteId;
    private List<ItemPedidoDTO> itens;
}

