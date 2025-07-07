package com.orderflow.product_service.controller;

import com.orderflow.product_service.domain.Produto;
import com.orderflow.product_service.dto.ProdutoRequestDTO;
import com.orderflow.product_service.dto.ProdutoResponseDTO;
import com.orderflow.product_service.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @PostMapping
    public ProdutoResponseDTO criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        Produto produto = Produto.builder()
                .nome(dto.getNome())
                .sku(dto.getSku())
                .preco(dto.getPreco())
                .build();

        return mapToResponse(service.salvar(produto));
    }

    @GetMapping
    public List<ProdutoResponseDTO> listar() {
        return service.listarTodos().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscar(@PathVariable Long id) {
        return mapToResponse(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        Produto produto = Produto.builder()
                .nome(dto.getNome())
                .sku(dto.getSku())
                .preco(dto.getPreco())
                .build();

        return mapToResponse(service.atualizar(id, produto));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    private ProdutoResponseDTO mapToResponse(Produto produto) {
        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .sku(produto.getSku())
                .preco(produto.getPreco())
                .build();
    }
}
