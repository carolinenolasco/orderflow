package com.orderflow.order_receiver_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRequestDTO {

    @NotNull
    private Long clienteId;

    @NotNull
    private List<ItemPedidoDTO> itens;
}
