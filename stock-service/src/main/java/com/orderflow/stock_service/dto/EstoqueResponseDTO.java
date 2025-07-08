package com.orderflow.stock_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstoqueResponseDTO {
    private Long id;
    private Long productId;
    private Integer quantidade;
}

