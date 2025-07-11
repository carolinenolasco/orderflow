package com.orderflow.order_receiver_service.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PagamentoRequestDTO {

    private Long pedidoId;
    private Double valor;
    private String numeroCartao;
}

