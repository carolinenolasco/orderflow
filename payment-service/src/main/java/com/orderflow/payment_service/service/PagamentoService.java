package com.orderflow.payment_service.service;

import com.orderflow.payment_service.dto.PagamentoRequestDTO;
import com.orderflow.payment_service.dto.PagamentoResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PagamentoService {

    public PagamentoResponseDTO processarPagamento(PagamentoRequestDTO dto) {
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
