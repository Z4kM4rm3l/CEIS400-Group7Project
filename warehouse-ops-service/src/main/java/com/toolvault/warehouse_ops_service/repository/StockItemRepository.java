package com.toolvault.warehouse_ops_service.repository;

import com.toolvault.warehouse_ops_service.domain.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    Optional<StockItem> findBySku(String sku);
    List<StockItem> findTop5ByOrderByQuantityDesc();
}
