package com.orderflow.payment_service.service;

import com.orderflow.payment_service.dto.PagamentoRequestDTO;
import com.orderflow.payment_service.dto.PagamentoResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PagamentoService {

    // Resultado já processado de cada pedido (garante que o mesmo pedido nunca é cobrado duas vezes)
    private final Map<Long, PagamentoResponseDTO> processados = new ConcurrentHashMap<>();

    public PagamentoResponseDTO processarPagamento(PagamentoRequestDTO dto) {
        // computeIfAbsent é atômico: chamadas simultâneas para o mesmo pedido processam uma única vez
        return processados.computeIfAbsent(dto.getPedidoId(), id -> executar(dto));
    }

    private PagamentoResponseDTO executar(PagamentoRequestDTO dto) {
        // Simula cartão inválido se começa com 0000
        if (dto.getNumeroCartao().startsWith("0000")) {
            return PagamentoResponseDTO.builder()
                    .pedidoId(dto.getPedidoId())
                    .status("FALHA")
                    .mensagem("Cartão recusado")
                    .build();
        }

        // Simula aprovação aleatória (80% chance de sucesso)
        boolean aprovado = new Random().nextDouble() < 0.8;

        return PagamentoResponseDTO.builder()
                .pedidoId(dto.getPedidoId())
                .status(aprovado ? "SUCESSO" : "FALHA")
                .mensagem(aprovado ? "Pagamento aprovado" : "Saldo insuficiente")
                .build();
    }
}
