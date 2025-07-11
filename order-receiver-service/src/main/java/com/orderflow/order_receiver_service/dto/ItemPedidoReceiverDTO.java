package com.orderflow.order_receiver_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ItemPedidoReceiverDTO {

    @NotNull
    private Long produtoId;

    @Min(1)
    private Integer quantidade;
}
