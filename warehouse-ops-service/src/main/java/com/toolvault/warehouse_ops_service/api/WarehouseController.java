package com.toolvault.warehouse_ops_service.api;

import com.toolvault.warehouse_ops_service.api.dto.AllocateRequest;
import com.toolvault.warehouse_ops_service.api.dto.RestockRequest;
import com.toolvault.warehouse_ops_service.domain.StockItem;
import com.toolvault.warehouse_ops_service.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
@Validated
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @Operation(summary = "List stock items")
    @GetMapping("/stock")
    public ResponseEntity<List<StockItem>> listStock() {
        return ResponseEntity.ok(service.listStock());
    }

    @Operation(summary = "Top SKUs by quantity (Top 5)")
    @GetMapping("/top-skus")
    public ResponseEntity<List<StockItem>> topSkus() {
        return ResponseEntity.ok(service.topSkus());
    }

    @Operation(summary = "Restock a SKU (increment quantity)")
    @PostMapping("/restock")
    public ResponseEntity<StockItem> restock(@RequestBody @Validated RestockRequest req) {
        return ResponseEntity.ok(service.restock(req.sku(), req.quantity(), req.name()));
    }

    @Operation(summary = "Allocate/pick a SKU (decrement quantity)")
    @PostMapping("/allocate")
    public ResponseEntity<StockItem> allocate(@RequestBody @Validated AllocateRequest req) {
        return ResponseEntity.ok(service.allocate(req.sku(), req.quantity()));
    }
}
