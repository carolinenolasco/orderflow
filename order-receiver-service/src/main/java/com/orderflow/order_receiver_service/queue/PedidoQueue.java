package com.orderflow.order_receiver_service.queue;

import com.orderflow.order_receiver_service.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PedidoQueue {

    private final RabbitTemplate rabbitTemplate;

    public PedidoQueue(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void adicionar(PedidoNaFila pedido) {
        rabbitTemplate.convertAndSend(RabbitConfig.FILA_PEDIDOS, pedido);
    }
}
