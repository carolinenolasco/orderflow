package com.orderflow.order_receiver_service.queue;

import com.orderflow.order_receiver_service.dto.ItemPedidoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class PedidoNaFila {
    private final Long pedidoId;
    private final List<ItemPedidoDTO> itens;
    private final BigDecimal valorTotal;
    private final String numeroCartao;
}
