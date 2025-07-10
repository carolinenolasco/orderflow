package com.orderflow.order_receiver_service.service;

import com.orderflow.order_receiver_service.client.CustomerClient;
import com.orderflow.order_receiver_service.client.ProductClient;
import com.orderflow.order_receiver_service.client.StockClient;
import com.orderflow.order_receiver_service.client.OrderClient;
import com.orderflow.order_receiver_service.dto.*;
import com.orderflow.order_receiver_service.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public OrderResponseDTO processarPedido(OrderRequestDTO orderRequest) {
        // Validar cliente
        CustomerDTO cliente = customerClient.getCustomerById(orderRequest.getCustomerId());
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado.");
        }

        // Validar e obter produtos
        List<ProductDTO> produtos = orderRequest.getItems().stream()
                .map(item -> {
                    ProductDTO produto = productClient.getProductBySku(item.getSku());
                    if (produto == null) {
                        throw new RuntimeException("Produto SKU " + item.getSku() + " não encontrado.");
                    }
                    return produto;
                })
                .collect(Collectors.toList());

        // Calcular valor total
        double total = orderRequest.getItems().stream()
                .mapToDouble(item -> {
                    ProductDTO produto = produtos.stream()
                            .filter(p -> p.getSku().equals(item.getSku()))
                            .findFirst()
                            .orElseThrow();
                    return produto.getPreco() * item.getQuantidade();
                })
                .sum();

        // Gerar pedido com status ABERTO
        OrderDTO novoPedido = new OrderDTO();
        novoPedido.setId(UUID.randomUUID().toString());
        novoPedido.setCustomerId(orderRequest.getCustomerId());
        novoPedido.setItems(orderRequest.getItems());
        novoPedido.setTotal(total);
        novoPedido.setStatus(OrderStatus.ABERTO.name());

        // Salvar pedido (chamada para order-service)
        OrderDTO pedidoCriado = orderClient.criarPedido(novoPedido);

        // Enviar pedido para fila (simulação)
        enviarParaFila(pedidoCriado);

        return new OrderResponseDTO(pedidoCriado.getId(), pedidoCriado.getStatus());
    }

    private void enviarParaFila(OrderDTO pedido) {
        // Aqui você pode implementar envio real para RabbitMQ ou Kafka
        // Este método simula o envio para processamento posterior
        System.out.println("Pedido enviado para fila: " + pedido.getId());
    }
}
