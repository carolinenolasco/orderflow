package com.orderflow.customer_service.controller;

import com.orderflow.customer_service.domain.Cliente;
import com.orderflow.customer_service.dto.ClienteRequestDTO;
import com.orderflow.customer_service.dto.ClienteResponseDTO;
import com.orderflow.customer_service.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    public ClienteResponseDTO criar(@Valid @RequestBody ClienteRequestDTO dto) {
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .dataNascimento(dto.getDataNascimento())
                .endereco(dto.getEndereco())
                .build();

        Cliente salvo = service.salvar(cliente);

        return mapToResponse(salvo);
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return service.listarTodos().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscar(@PathVariable Long id) {
        return mapToResponse(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .dataNascimento(dto.getDataNascimento())
                .endereco(dto.getEndereco())
                .build();

        Cliente atualizado = service.atualizar(id, cliente);
        return mapToResponse(atualizado);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    private ClienteResponseDTO mapToResponse(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpf(cliente.getCpf())
                .dataNascimento(cliente.getDataNascimento())
                .endereco(cliente.getEndereco())
                .build();
    }
}

