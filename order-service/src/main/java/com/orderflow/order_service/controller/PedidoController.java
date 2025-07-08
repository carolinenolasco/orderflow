package com.orderflow.order_service.controller;

import com.orderflow.order_service.domain.Pedido;
import com.orderflow.order_service.domain.StatusPedido;
import com.orderflow.order_service.dto.ItemPedidoDTO;
import com.orderflow.order_service.dto.PedidoRequestDTO;
import com.orderflow.order_service.dto.PedidoResponseDTO;
import com.orderflow.order_service.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    public PedidoResponseDTO criar(@Valid @RequestBody PedidoRequestDTO dto) {
        Pedido pedido = service.criar(dto);
        return mapToResponse(pedido);
    }

    @GetMapping
    public List<PedidoResponseDTO> listar() {
        return service.listarTodos().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPorId(@PathVariable Long id) {
        return mapToResponse(service.buscarPorId(id));
    }

    @PutMapping("/{id}/status")
    public PedidoResponseDTO atualizarStatus(@PathVariable Long id, @RequestParam StatusPedido status) {
        return mapToResponse(service.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    private PedidoResponseDTO mapToResponse(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .dataPedido(pedido.getDataPedido())
                .status(pedido.getStatus())
                .itens(pedido.getItens().stream().map(item ->
                        ItemPedidoDTO.builder()
                                .produtoId(item.getProdutoId())
                                .quantidade(item.getQuantidade())
                                .precoUnitario(item.getPrecoUnitario())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }
}
