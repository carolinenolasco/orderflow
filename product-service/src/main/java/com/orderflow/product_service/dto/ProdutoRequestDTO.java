package com.orderflow.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoRequestDTO {
    @NotBlank
    private String nome;

    @NotBlank
    private String sku;

    @PositiveOrZero
    private BigDecimal preco;
}
