package com.orderflow.stock_service.service;

import com.orderflow.stock_service.domain.Estoque;
import com.orderflow.stock_service.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository repository;

    public Estoque salvar(Estoque estoque) {
        Optional<Estoque> existente = repository.findByProductId(estoque.getProductId());
        if (existente.isPresent()) {
            throw new RuntimeException("Produto já possui controle de estoque");
        }
        return repository.save(estoque);
    }

    public List<Estoque> listarTodos() {
        return repository.findAll();
    }

    public Estoque buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
    }

    public Estoque atualizar(Long id, Estoque atualizado) {
        Estoque estoque = buscarPorId(id);
        estoque.setQuantidade(atualizado.getQuantidade());
        return repository.save(estoque);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Estoque buscarPorProductId(Long productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Estoque para o produto não encontrado"));
    }
}
