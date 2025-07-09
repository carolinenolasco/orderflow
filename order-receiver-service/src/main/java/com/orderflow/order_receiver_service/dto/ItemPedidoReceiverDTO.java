package com.orderflow.order_receiver_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoReceiverDTO {

    @NotNull(message = "Produto obrigatório")
    private Long produtoId;

    @Min(value = 1, message = "Quantidade deve ser maior que 0")
    private Integer quantidade;
}
