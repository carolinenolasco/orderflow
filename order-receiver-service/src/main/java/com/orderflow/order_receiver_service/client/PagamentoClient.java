package com.orderflow.order_receiver_service.client;

import com.orderflow.order_receiver_service.dto.PagamentoRequestDTO;
import com.orderflow.order_receiver_service.dto.PagamentoResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PagamentoClient {

    @PostMapping("/pagamentos")
    PagamentoResponseDTO pagar(@RequestBody PagamentoRequestDTO dto);
}
