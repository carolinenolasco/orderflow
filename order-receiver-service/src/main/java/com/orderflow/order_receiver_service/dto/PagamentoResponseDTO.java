package com.orderflow.order_receiver_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoResponseDTO {

    private Long pedidoId;
    private String status;
    private String mensagem;
}
