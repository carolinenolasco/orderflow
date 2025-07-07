package com.orderflow.product_service.repository;

import com.orderflow.product_service.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository  extends JpaRepository<Produto, Long> {
    Optional<Produto> findBySku(String sku);
}
