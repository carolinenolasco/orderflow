package com.orderflow.order_receiver_service.service;

import com.orderflow.order_receiver_service.client.*;
import com.orderflow.order_receiver_service.domain.StatusPedido;
import com.orderflow.order_receiver_service.dto.ItemPedidoDTO;
import com.orderflow.order_receiver_service.dto.PagamentoRequestDTO;
import com.orderflow.order_receiver_service.dto.PagamentoResponseDTO;
import com.orderflow.order_receiver_service.dto.PedidoReceiverRequestDTO;
import com.orderflow.order_receiver_service.queue.PedidoNaFila;
import com.orderflow.order_receiver_service.queue.PedidoQueue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoReceiverServiceTest {

    @Mock private CustomerClient customerClient;
    @Mock private ProductClient productClient;
    @Mock private StockClient stockClient;
    @Mock private OrderClient orderClient;
    @Mock private PagamentoClient pagamentoClient;
    @Mock private PedidoQueue pedidoQueue;

    @InjectMocks
    private PedidoReceiverService service;

    private static final Long PEDIDO_ID = 1L;

    private ItemPedidoDTO item(Long produtoId, int quantidade) {
        return ItemPedidoDTO.builder()
                .produtoId(produtoId)
                .quantidade(quantidade)
                .precoUnitario(new BigDecimal("50.00"))
                .build();
    }

    private PedidoNaFila pedidoComDoisItens() {
        return new PedidoNaFila(PEDIDO_ID,
                List.of(item(1L, 1), item(2L, 2)),
                new BigDecimal("150.00"),
                "2222333344445555");
    }

    private PagamentoResponseDTO resposta(String status) {
        return PagamentoResponseDTO.builder().pedidoId(PEDIDO_ID).status(status).build();
    }

    @Test
    @DisplayName("Pagamento aprovado: fecha com sucesso e não repõe estoque")
    void processar_sucesso() {
        when(pagamentoClient.pagar(any(PagamentoRequestDTO.class))).thenReturn(resposta("SUCESSO"));

        service.processar(pedidoComDoisItens());

        verify(stockClient).baixar(1L, 1);
        verify(stockClient).baixar(2L, 2);
        verify(stockClient, never()).repor(any(), any());
        verify(orderClient).atualizarStatus(PEDIDO_ID, StatusPedido.FECHADO_COM_SUCESSO);
    }

    @Test
    @DisplayName("Pagamento recusado: repõe todo o estoque e fecha sem crédito")
    void processar_pagamentoRecusado() {
        when(pagamentoClient.pagar(any(PagamentoRequestDTO.class))).thenReturn(resposta("FALHA"));

        service.processar(pedidoComDoisItens());

        verify(stockClient).repor(1L, 1);
        verify(stockClient).repor(2L, 2);
        verify(orderClient).atualizarStatus(PEDIDO_ID, StatusPedido.FECHADO_SEM_CREDITO);
        verify(orderClient, never()).atualizarStatus(PEDIDO_ID, StatusPedido.FECHADO_COM_SUCESSO);
    }

    @Test
    @DisplayName("Erro ao chamar o pagamento: trata como recusado e repõe o estoque")
    void processar_erroNoPagamento() {
        when(pagamentoClient.pagar(any(PagamentoRequestDTO.class)))
                .thenThrow(new RuntimeException("payment-service fora do ar"));

        service.processar(pedidoComDoisItens());

        verify(stockClient).repor(1L, 1);
        verify(stockClient).repor(2L, 2);
        verify(orderClient).atualizarStatus(PEDIDO_ID, StatusPedido.FECHADO_SEM_CREDITO);
    }

    @Test
    @DisplayName("Falta de estoque no segundo item: devolve só o primeiro, não cobra e fecha sem estoque")
    void processar_semEstoque() {
        doNothing().when(stockClient).baixar(1L, 1);
        doThrow(new RuntimeException("Estoque insuficiente"))
                .when(stockClient).baixar(2L, 2);

        service.processar(pedidoComDoisItens());

        verify(stockClient).baixar(1L, 1);
        verify(stockClient).repor(1L, 1);
        verify(stockClient, never()).repor(eq(2L), any());
        verifyNoInteractions(pagamentoClient);
        verify(orderClient).atualizarStatus(PEDIDO_ID, StatusPedido.FECHADO_SEM_ESTOQUE);
    }

    @Test
    @DisplayName("Cliente inexistente: lança erro e não cria pedido nem enfileira")
    void registrar_clienteInexistente() {
        when(customerClient.buscarPorId(99L)).thenThrow(new RuntimeException("404"));

        PedidoReceiverRequestDTO dto = new PedidoReceiverRequestDTO(99L, List.of(), "2222333344445555");

        assertThrows(RuntimeException.class, () -> service.registrar(dto));

        verifyNoInteractions(orderClient, pedidoQueue);
    }
}
