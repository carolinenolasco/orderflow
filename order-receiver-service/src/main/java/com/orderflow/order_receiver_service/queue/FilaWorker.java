package com.orderflow.order_receiver_service.queue;

import com.orderflow.order_receiver_service.dto.PedidoReceiverRequestDTO;
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
            while (true) {
                try {
                    PedidoReceiverRequestDTO pedido = pedidoQueue.proximoPedido();
                    log.info("🛠️ Processando pedido da fila: cliente {}", pedido.getClienteId());

                    receiverService.processarPedido(pedido);

                    log.info("✅ Pedido processado com sucesso.");
                } catch (Exception e) {
                    log.error("❌ Erro ao processar pedido da fila: " + e.getMessage());
                }
            }
        });

        worker.setDaemon(true);
        worker.start();
    }
}
