package com.orderflow.order_receiver_service.queue;

import com.orderflow.order_receiver_service.service.PedidoReceiverService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FilaWorker {

    @Autowired
    private PedidoQueue pedidoQueue;

    @Autowired
    private PedidoReceiverService receiverService;

    @PostConstruct
    public void iniciar() {
        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    PedidoNaFila pedido = pedidoQueue.proximoPedido();
                    log.info("Processando pedido {}", pedido.getPedidoId());
                    receiverService.processar(pedido);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    log.error("Erro ao processar pedido da fila: {}", e.getMessage(), e);
                }
            }
        }, "fila-pedidos-worker");

        worker.setDaemon(true);
        worker.start();
    }
}
