package com.orderflow.order_receiver_service.queue;

import com.orderflow.order_receiver_service.dto.PedidoReceiverRequestDTO;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PedidoQueue {

    private final BlockingQueue<PedidoReceiverRequestDTO> fila = new LinkedBlockingQueue<>();

    public void adicionar(PedidoReceiverRequestDTO pedido) {
        fila.add(pedido);
    }

    public PedidoReceiverRequestDTO proximoPedido() throws InterruptedException {
        return fila.take(); // bloqueia até ter pedido
    }

    public boolean temPedidos() {
        return !fila.isEmpty();
    }
}
