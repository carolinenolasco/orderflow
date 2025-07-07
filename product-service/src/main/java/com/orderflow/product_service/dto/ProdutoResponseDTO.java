package com.orderflow.product_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String sku;
    private BigDecimal preco;
}
