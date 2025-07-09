package com.orderflow.order_receiver_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoRequestDTO {

    @NotNull
    private Long pedidoId;

    @NotNull
    private Double valor;

    @NotNull
    private String numeroCartao;
}
