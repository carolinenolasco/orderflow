package com.orderflow.payment_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoRequestDTO {

    @NotNull(message = "ID do pedido obrigatório")
    private Long pedidoId;

    @NotNull(message = "Valor do pedido obrigatório")
    @Min(value = 1, message = "Valor deve ser maior que zero")
    private Double valor;

    @NotNull(message = "Número do cartão obrigatório")
    private String numeroCartao;
}
