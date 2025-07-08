package com.orderflow.stock_service.controller;

import com.orderflow.stock_service.domain.Estoque;
import com.orderflow.stock_service.dto.EstoqueRequestDTO;
import com.orderflow.stock_service.dto.EstoqueResponseDTO;
import com.orderflow.stock_service.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    @Autowired
    private EstoqueService service;

    @PostMapping
    public EstoqueResponseDTO criar(@Valid @RequestBody EstoqueRequestDTO dto) {
        Estoque estoque = Estoque.builder()
                .productId(dto.getProductId())
                .quantidade(dto.getQuantidade())
                .build();

        return mapToResponse(service.salvar(estoque));
    }

    @GetMapping
    public List<EstoqueResponseDTO> listar() {
        return service.listarTodos()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EstoqueResponseDTO buscarPorId(@PathVariable Long id) {
        return mapToResponse(service.buscarPorId(id));
    }

    @GetMapping("/produto/{productId}")
    public EstoqueResponseDTO buscarPorProductId(@PathVariable Long productId) {
        return mapToResponse(service.buscarPorProductId(productId));
    }

    @PutMapping("/{id}")
    public EstoqueResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody EstoqueRequestDTO dto) {
        Estoque estoque = Estoque.builder()
                .productId(dto.getProductId())
                .quantidade(dto.getQuantidade())
                .build();

        return mapToResponse(service.atualizar(id, estoque));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    private EstoqueResponseDTO mapToResponse(Estoque estoque) {
        return EstoqueResponseDTO.builder()
                .id(estoque.getId())
                .productId(estoque.getProductId())
                .quantidade(estoque.getQuantidade())
                .build();
    }
}
