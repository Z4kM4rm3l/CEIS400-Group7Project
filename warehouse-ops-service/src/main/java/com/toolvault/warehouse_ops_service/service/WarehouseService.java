package com.toolvault.warehouse_ops_service.service;

import com.toolvault.warehouse_ops_service.domain.StockItem;
import com.toolvault.warehouse_ops_service.repository.StockItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    private final StockItemRepository stock;

    public WarehouseService(StockItemRepository stock) {
        this.stock = stock;
    }

    public List<StockItem> listStock() {
        return stock.findAll();
    }

    public List<StockItem> topSkus() {
        return stock.findTop5ByOrderByQuantityDesc();
    }

    @Transactional
    public StockItem restock(String sku, int qty, String name) {
        StockItem item = stock.findBySku(sku).orElseGet(StockItem::new);
        item.setSku(sku);
        if (name != null && !name.isBlank()) item.setName(name);
        item.setQuantity(item.getQuantity() + qty);
        return stock.save(item);
    }

    @Transactional
    public StockItem allocate(String sku, int qty) {
        StockItem item = stock.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + sku));
        if (item.getQuantity() < qty) {
            throw new IllegalArgumentException("Insufficient stock for " + sku);
        }
        item.setQuantity(item.getQuantity() - qty);
        return stock.save(item);
    }
}
