package com.orderflow.order_service.dto;

import com.orderflow.order_service.domain.StatusPedido;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {

    private Long id;
    private Long clienteId;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private List<ItemPedidoDTO> itens;
}
