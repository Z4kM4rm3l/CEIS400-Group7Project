package com.toolvault.depot_ops_service.api;

import com.toolvault.depot_ops_service.api.dto.CheckInRequest;
import com.toolvault.depot_ops_service.api.dto.CheckOutRequest;
import com.toolvault.depot_ops_service.api.dto.TransferRequestDto;
import com.toolvault.depot_ops_service.domain.Asset;
import com.toolvault.depot_ops_service.domain.TransferRequest;
import com.toolvault.depot_ops_service.service.DepotService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depot")
@Validated
public class DepotController {

    private final DepotService service;

    public DepotController(DepotService service) {
        this.service = service;
    }

    @Operation(summary = "List assets")
    @GetMapping("/assets")
    public ResponseEntity<List<Asset>> listAssets() {
        return ResponseEntity.ok(service.listAssets());
    }

    @Operation(summary = "Check-in asset")
    @PostMapping("/assets/checkin")
    public ResponseEntity<Asset> checkIn(@RequestBody @Validated CheckInRequest req) {
        return ResponseEntity.ok(service.checkIn(req.tag(), req.model()));
    }

    @Operation(summary = "Check-out asset")
    @PostMapping("/assets/checkout")
    public ResponseEntity<Asset> checkOut(@RequestBody @Validated CheckOutRequest req) {
        return ResponseEntity.ok(service.checkOut(req.tag()));
    }

    @Operation(summary = "Request asset transfer to a warehouse")
    @PostMapping("/transfers/request")
    public ResponseEntity<TransferRequest> transfer(@RequestBody @Validated TransferRequestDto req) {
        return ResponseEntity.ok(service.requestTransfer(req.assetTag(), req.destWarehouseCode()));
    }
}
