package com.orderflow.order_receiver_service.queue;

import com.orderflow.order_receiver_service.dto.ItemPedidoDTO;

import java.math.BigDecimal;
import java.util.List;

public record PedidoNaFila(
        Long pedidoId,
        List<ItemPedidoDTO> itens,
        BigDecimal valorTotal,
        String numeroCartao) {
}
