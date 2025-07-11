package com.orderflow.order_receiver_service.service;

import com.orderflow.order_receiver_service.client.*;
import com.orderflow.order_receiver_service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoReceiverService {

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private StockClient stockClient;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private PagamentoClient pagamentoClient;

    public PedidoResponseDTO processarPedido(PedidoReceiverRequestDTO receiverDTO) {
        // Verifica se o cliente existe
        try {
            customerClient.buscarPorId(receiverDTO.getClienteId());
        } catch (Exception e) {
            throw new RuntimeException("Cliente não encontrado: ID " + receiverDTO.getClienteId());
        }

        // Lista final com preços para montar o pedido
        List<ItemPedidoDTO> itensComPreco = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoReceiverDTO item : receiverDTO.getItens()) {
            // Verifica se o produto existe
            var produto = productClient.buscarPorId(item.getProdutoId());

            // Verifica se há estoque suficiente
            var estoque = stockClient.buscarPorProductId(item.getProdutoId());
            if (estoque.quantidade() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto ID " + item.getProdutoId());
            }

            // Soma valor total do pedido
            BigDecimal preco = produto.preco();
            valorTotal = valorTotal.add(preco.multiply(BigDecimal.valueOf(item.getQuantidade())));

            // Adiciona item formatado com preço
            itensComPreco.add(ItemPedidoDTO.builder()
                    .produtoId(item.getProdutoId())
                    .quantidade(item.getQuantidade())
                    .precoUnitario(preco)
                    .build());
        }

        // Cria o pedido no order-service
        PedidoRequestDTO pedidoDTO = PedidoRequestDTO.builder()
                .clienteId(receiverDTO.getClienteId())
                .itens(itensComPreco)
                .build();

        PedidoResponseDTO pedidoCriado = orderClient.criarPedido(pedidoDTO);

        // Realiza o pagamento com base no valor total
        PagamentoRequestDTO pagamento = PagamentoRequestDTO.builder()
                .pedidoId(pedidoCriado.getId())
                .valor(valorTotal.doubleValue())
                .numeroCartao("1234567890123456") // simulado
                .build();

        PagamentoResponseDTO resultadoPagamento = pagamentoClient.pagar(pagamento);

        // Define o status final do pedido
        String statusFinal = resultadoPagamento.getStatus().equalsIgnoreCase("SUCESSO")
                ? "FECHADO_COM_SUCESSO"
                : "FECHADO_SEM_CREDITO";

        // Atualiza o status do pedido
        orderClient.atualizarStatus(pedidoCriado.getId(), statusFinal);

        return pedidoCriado;
    }
}
