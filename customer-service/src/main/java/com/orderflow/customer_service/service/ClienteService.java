package com.orderflow.customer_service.service;

import com.orderflow.customer_service.domain.Cliente;
import com.orderflow.customer_service.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public Cliente salvar(Cliente cliente) {
        Optional<Cliente> existente = repository.findByCpf(cliente.getCpf());
        if(existente.isPresent()) {
            throw new RuntimeException("CPF já cadastrado");
        }
        return repository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("ID inexistente"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente cliente = buscarPorId(id);
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEndereco(clienteAtualizado.getEndereco());
        cliente.setDataNascimento(clienteAtualizado.getDataNascimento());
        return repository.save(cliente);
    }
}
