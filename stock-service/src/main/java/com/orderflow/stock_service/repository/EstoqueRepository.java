package com.orderflow.stock_service.repository;

import com.orderflow.stock_service.domain.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque,Long> {
    Optional<Estoque> findByProductId(Long productId);
}
