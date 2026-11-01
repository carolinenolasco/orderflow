package com.orderflow.order_receiver_service.queue;

import com.orderflow.order_receiver_service.config.RabbitConfig;
import com.orderflow.order_receiver_service.service.PedidoReceiverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PedidoConsumer {

    @Autowired
    private PedidoReceiverService service;

    @RabbitListener(queues = RabbitConfig.FILA_PEDIDOS)
    public void consumir(PedidoNaFila pedido) {
        log.info("Pedido {} recebido da fila", pedido.pedidoId());
        service.processar(pedido);
    }
}
