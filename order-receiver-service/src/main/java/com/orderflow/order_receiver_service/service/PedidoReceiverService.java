package com.orderflow.order_receiver_service.service;

import com.orderflow.order_receiver_service.client.*;
import com.orderflow.order_receiver_service.domain.StatusPedido;
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

        // Lista com itens preenchidos com preço
        List<ItemPedidoDTO> itensComPreco = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoReceiverDTO item : receiverDTO.getItens()) {
            // Verifica produto
            var produto = productClient.buscarPorId(item.getProdutoId());

            // Verifica estoque
            var estoque = stockClient.buscarPorProductId(item.getProdutoId());
            if (estoque.quantidade() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para produto ID " + item.getProdutoId());
            }

            // Soma valor
            BigDecimal preco = produto.preco();
            valorTotal = valorTotal.add(preco.multiply(BigDecimal.valueOf(item.getQuantidade())));

            itensComPreco.add(ItemPedidoDTO.builder()
                    .produtoId(item.getProdutoId())
                    .quantidade(item.getQuantidade())
                    .precoUnitario(preco)
                    .build());
        }

        // Cria pedido no order-service
        PedidoRequestDTO pedidoDTO = PedidoRequestDTO.builder()
                .clienteId(receiverDTO.getClienteId())
                .itens(itensComPreco)
                .build();

        PedidoResponseDTO pedidoCriado = orderClient.criarPedido(pedidoDTO);

        // Realiza pagamento
        PagamentoRequestDTO pagamento = PagamentoRequestDTO.builder()
                .pedidoId(pedidoCriado.getId())
                .valor(valorTotal.doubleValue())
                .numeroCartao("1234567890123456")
                .build();

        PagamentoResponseDTO resultadoPagamento = pagamentoClient.pagar(pagamento);

        // Define status
        StatusPedido status = resultadoPagamento.getStatus().equalsIgnoreCase("SUCESSO")
                ? StatusPedido.FECHADO_COM_SUCESSO
                : StatusPedido.FECHADO_SEM_CREDITO;

        orderClient.atualizarStatus(pedidoCriado.getId(), StatusPedido.valueOf(status.name()));

        return PedidoResponseDTO.builder()
                .id(pedidoCriado.getId())
                .clienteId(pedidoCriado.getClienteId())
                .dataPedido(pedidoCriado.getDataPedido())
                .status(status)
                .itens(itensComPreco)
                .build();
    }
}
