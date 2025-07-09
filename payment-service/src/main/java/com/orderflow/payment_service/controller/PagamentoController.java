package com.orderflow.payment_service.controller;

import com.orderflow.payment_service.dto.PagamentoRequestDTO;
import com.orderflow.payment_service.dto.PagamentoResponseDTO;
import com.orderflow.payment_service.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public PagamentoResponseDTO pagar(@Valid @RequestBody PagamentoRequestDTO dto) {
        return pagamentoService.processarPagamento(dto);
    }
}
