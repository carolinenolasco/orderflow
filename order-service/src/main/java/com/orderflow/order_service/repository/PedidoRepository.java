package com.orderflow.order_service.repository;

import com.orderflow.order_service.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
