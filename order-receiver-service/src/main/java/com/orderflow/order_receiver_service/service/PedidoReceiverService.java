package com.orderflow.order_receiver_service.service;

import com.orderflow.order_receiver_service.client.CustomerClient;
import com.orderflow.order_receiver_service.client.ProductClient;
import com.orderflow.order_receiver_service.client.StockClient;
import com.orderflow.order_receiver_service.client.OrderClient;
import com.orderflow.order_receiver_service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PedidoResponseDTO processarPedido(PedidoReceiverRequestDTO receiverDTO) {
        // Verifica se o cliente existe
        try {
            customerClient.buscarPorId(receiverDTO.getClienteId());
        } catch (Exception e) {
            throw new RuntimeException("Cliente não encontrado: ID " + receiverDTO.getClienteId());
        }

        // Para cada item, verificar produto e estoque
        for (ItemPedidoReceiverDTO item : receiverDTO.getItens()) {
            // Verifica se o produto existe
            try {
                productClient.buscarPorId(item.getProdutoId());
            } catch (Exception e) {
                throw new RuntimeException("Produto não encontrado: ID " + item.getProdutoId());
            }

            // Verifica se há estoque suficiente
            try {
                var estoque = stockClient.buscarPorProductId(item.getProdutoId());
                if (estoque.quantidade() < item.getQuantidade()) {
                    throw new RuntimeException("Estoque insuficiente para o produto ID " + item.getProdutoId());
                }
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao verificar estoque do produto ID " + item.getProdutoId());
            }
        }

        // Monta o DTO que será enviado para o order-service
        var pedidoDTO = PedidoRequestDTO.builder()
                .clienteId(receiverDTO.getClienteId())
                .itens(receiverDTO.getItens().stream().map(i ->
                        ItemPedidoDTO.builder()
                                .produtoId(i.getProdutoId())
                                .quantidade(i.getQuantidade())
                                .precoUnitario(null) // será tratado no order-service
                                .build()
                ).toList())
                .build();

        return orderClient.criarPedido(pedidoDTO);
    }
}
