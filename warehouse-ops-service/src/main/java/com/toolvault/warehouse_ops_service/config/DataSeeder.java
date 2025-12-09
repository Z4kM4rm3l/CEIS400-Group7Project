package com.toolvault.warehouse_ops_service.config;

import com.toolvault.warehouse_ops_service.domain.StockItem;
import com.toolvault.warehouse_ops_service.repository.StockItemRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Profile("!test")
public class DataSeeder {
    private final StockItemRepository stock;

    public DataSeeder(StockItemRepository stock) { this.stock = stock; }

    @PostConstruct
    public void seed() {
        if (stock.count() == 0) {
            StockItem s1 = new StockItem(); s1.setSku("SKU-100"); s1.setName("Hammer"); s1.setQuantity(50);
            StockItem s2 = new StockItem(); s2.setSku("SKU-200"); s2.setName("Cordless Drill"); s2.setQuantity(35);
            StockItem s3 = new StockItem(); s3.setSku("SKU-300"); s3.setName("Saw"); s3.setQuantity(20);
            stock.save(s1); stock.save(s2); stock.save(s3);
        }
    }
}
