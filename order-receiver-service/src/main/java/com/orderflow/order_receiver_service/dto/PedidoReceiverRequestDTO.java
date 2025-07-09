package com.orderflow.order_receiver_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoReceiverRequestDTO {

    @NotNull(message = "Cliente obrigatório")
    private Long clienteId;

    @NotNull(message = "Lista de itens obrigatória")
    private List<ItemPedidoReceiverDTO> itens;
}
