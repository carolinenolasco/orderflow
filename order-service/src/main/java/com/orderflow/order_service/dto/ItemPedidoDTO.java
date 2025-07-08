package com.orderflow.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoDTO {

    @NotNull(message = "Produto obrigatório")
    private Long produtoId;

    @Min(value = 1, message = "Quantidade deve ser maior que 0")
    private Integer quantidade;

    @NotNull(message = "Preço unitário obrigatório")
    private BigDecimal precoUnitario;
}
