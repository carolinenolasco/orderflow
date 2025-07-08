package com.orderflow.order_service.service;

import com.orderflow.order_service.domain.ItemPedido;
import com.orderflow.order_service.domain.Pedido;
import com.orderflow.order_service.domain.StatusPedido;
import com.orderflow.order_service.dto.ItemPedidoDTO;
import com.orderflow.order_service.dto.PedidoRequestDTO;
import com.orderflow.order_service.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public Pedido criar(PedidoRequestDTO dto) {
        // Criar entidade Pedido
        Pedido pedido = Pedido.builder()
                .clienteId(dto.getClienteId())
                .dataPedido(LocalDateTime.now())
                .status(StatusPedido.CRIADO)
                .build();

        // Criar lista de itens a partir do DTO
        List<ItemPedido> itens = dto.getItens().stream().map(itemDto ->
                ItemPedido.builder()
                        .pedido(pedido) // vínculo reverso
                        .produtoId(itemDto.getProdutoId())
                        .quantidade(itemDto.getQuantidade())
                        .precoUnitario(itemDto.getPrecoUnitario())
                        .build()
        ).toList();

        pedido.setItens(itens);

        // Salvar pedido e itens (em cascata)
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Transactional
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }
}
