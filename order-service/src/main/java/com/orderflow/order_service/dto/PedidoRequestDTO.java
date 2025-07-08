package com.orderflow.order_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRequestDTO {

    @NotNull(message = "Cliente obrigatório")
    private Long clienteId;

    @NotNull(message = "Lista de itens obrigatória")
    private List<ItemPedidoDTO> itens;
}
