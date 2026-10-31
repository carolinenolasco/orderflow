package com.orderflow.order_receiver_service.queue;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PedidoQueue {

    private final BlockingQueue<PedidoNaFila> fila = new LinkedBlockingQueue<>();

    public void adicionar(PedidoNaFila pedido) {
        fila.add(pedido);
    }

    public PedidoNaFila proximoPedido() throws InterruptedException {
        return fila.take();
    }
}
