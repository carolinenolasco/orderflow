package com.orderflow.order_receiver_service.service;

import com.orderflow.order_receiver_service.client.*;
import com.orderflow.order_receiver_service.domain.StatusPedido;
import com.orderflow.order_receiver_service.dto.*;
import com.orderflow.order_receiver_service.queue.PedidoNaFila;
import com.orderflow.order_receiver_service.queue.PedidoQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PedidoReceiverService {

    @Autowired private CustomerClient customerClient;
    @Autowired private ProductClient productClient;
    @Autowired private StockClient stockClient;
    @Autowired private OrderClient orderClient;
    @Autowired private PagamentoClient pagamentoClient;
    @Autowired private PedidoQueue pedidoQueue;

    /** Etapa síncrona: valida cliente e produtos, cria o pedido (CRIADO) e enfileira. */
    public PedidoResponseDTO registrar(PedidoReceiverRequestDTO dto) {
        try {
            customerClient.buscarPorId(dto.getClienteId());
        } catch (Exception e) {
            throw new RuntimeException("Cliente não encontrado: ID " + dto.getClienteId());
        }

        List<ItemPedidoDTO> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoReceiverDTO item : dto.getItens()) {
            ProductClient.ProdutoResponse produto;
            try {
                produto = productClient.buscarPorId(item.getProdutoId());
            } catch (Exception e) {
                throw new RuntimeException("Produto não encontrado: ID " + item.getProdutoId());
            }

            valorTotal = valorTotal.add(
                    produto.preco().multiply(BigDecimal.valueOf(item.getQuantidade())));

            itens.add(ItemPedidoDTO.builder()
                    .produtoId(item.getProdutoId())
                    .quantidade(item.getQuantidade())
                    .precoUnitario(produto.preco())
                    .build());
        }

        PedidoResponseDTO criado = orderClient.criarPedido(PedidoRequestDTO.builder()
                .clienteId(dto.getClienteId())
                .itens(itens)
                .build());

        pedidoQueue.adicionar(new PedidoNaFila(criado.getId(), itens, valorTotal, dto.getNumeroCartao()));

        return PedidoResponseDTO.builder()
                .id(criado.getId())
                .clienteId(criado.getClienteId())
                .dataPedido(criado.getDataPedido())
                .status(StatusPedido.CRIADO)
                .itens(itens)
                .build();
    }

    /** Etapa assíncrona: reserva estoque, cobra e compensa se algo falhar. */
    public void processar(PedidoNaFila pedido) {
        List<ItemPedidoDTO> baixados = new ArrayList<>();

        // 1) Baixa de estoque (se faltar algum item, devolve os que já baixou)
        try {
            for (ItemPedidoDTO item : pedido.itens()) {
                stockClient.baixar(item.getProdutoId(), item.getQuantidade());
                baixados.add(item);
            }
        } catch (Exception e) {
            log.warn("Pedido {} sem estoque: {}", pedido.pedidoId(), e.getMessage());
            reporEstoque(baixados);
            orderClient.atualizarStatus(pedido.pedidoId(), StatusPedido.FECHADO_SEM_ESTOQUE);
            return;
        }

        // 2) Pagamento (se recusar, devolve o estoque)
        boolean aprovado;
        try {
            PagamentoResponseDTO resposta = pagamentoClient.pagar(PagamentoRequestDTO.builder()
                    .pedidoId(pedido.pedidoId())
                    .valor(pedido.valorTotal().doubleValue())
                    .numeroCartao(pedido.numeroCartao())
                    .build());
            aprovado = "SUCESSO".equalsIgnoreCase(resposta.getStatus());
        } catch (Exception e) {
            log.warn("Falha ao pagar pedido {}: {}", pedido.pedidoId(), e.getMessage());
            aprovado = false;
        }

        if (!aprovado) {
            reporEstoque(baixados);
            orderClient.atualizarStatus(pedido.pedidoId(), StatusPedido.FECHADO_SEM_CREDITO);
            return;
        }

        // 3) Sucesso
        orderClient.atualizarStatus(pedido.pedidoId(), StatusPedido.FECHADO_COM_SUCESSO);
        log.info("Pedido {} fechado com sucesso", pedido.pedidoId());
    }

    private void reporEstoque(List<ItemPedidoDTO> itens) {
        for (ItemPedidoDTO item : itens) {
            try {
                stockClient.repor(item.getProdutoId(), item.getQuantidade());
            } catch (Exception e) {
                log.error("Não foi possível repor estoque do produto {}: {}",
                        item.getProdutoId(), e.getMessage());
            }
        }
    }
}
