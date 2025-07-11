package com.orderflow.order_receiver_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ItemPedidoDTO {

    @NotNull
    private Long produtoId;

    @NotNull
    private Integer quantidade;

    @NotNull
    private BigDecimal precoUnitario;
}

