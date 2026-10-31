package com.orderflow.order_receiver_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoReceiverRequestDTO {

    @NotNull
    private Long clienteId;

    @NotEmpty
    @Valid
    private List<ItemPedidoReceiverDTO> itens;

    @NotBlank
    private String numeroCartao;
}
