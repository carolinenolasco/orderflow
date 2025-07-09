package com.orderflow.payment_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoResponseDTO {

    private Long pedidoId;
    private String status; // SUCESSO ou FALHA
    private String mensagem;
}
