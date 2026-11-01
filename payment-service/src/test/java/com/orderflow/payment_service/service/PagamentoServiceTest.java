package com.orderflow.payment_service.service;

import com.orderflow.payment_service.dto.PagamentoRequestDTO;
import com.orderflow.payment_service.dto.PagamentoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PagamentoServiceTest {

    private final PagamentoService service = new PagamentoService();

    private PagamentoRequestDTO requisicao(Long pedidoId, String cartao) {
        return PagamentoRequestDTO.builder()
                .pedidoId(pedidoId)
                .valor(100.0)
                .numeroCartao(cartao)
                .build();
    }

    @Test
    @DisplayName("Cartão que começa com 0000 é sempre recusado")
    void cartaoInvalido_ehRecusado() {
        PagamentoResponseDTO resposta = service.processarPagamento(requisicao(1L, "0000333344445555"));

        assertEquals("FALHA", resposta.getStatus());
        assertEquals("Cartão recusado", resposta.getMensagem());
    }

    @Test
    @DisplayName("Mesmo pedido enviado várias vezes devolve sempre o mesmo resultado")
    void mesmoPedido_ehIdempotente() {
        PagamentoResponseDTO primeira = service.processarPagamento(requisicao(10L, "2222333344445555"));

        for (int i = 0; i < 50; i++) {
            PagamentoResponseDTO repetida = service.processarPagamento(requisicao(10L, "2222333344445555"));
            assertEquals(primeira.getStatus(), repetida.getStatus());
            assertEquals(primeira.getMensagem(), repetida.getMensagem());
        }
    }
}
