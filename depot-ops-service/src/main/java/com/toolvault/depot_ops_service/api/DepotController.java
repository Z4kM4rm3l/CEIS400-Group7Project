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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/depot")
@CrossOrigin(origins = "*")
@Validated
public class DepotController {

    private final DepotService service;

    public DepotController(DepotService service) {
        this.service = service;
    }

    @Operation(summary = "List all assets")
    @GetMapping("/assets")
    public ResponseEntity<List<Asset>> listAssets() {
        return ResponseEntity.ok(service.listAssets());
    }

    @Operation(summary = "Check-in an asset")
    @PostMapping("/assets/{tag}/checkin")
    public ResponseEntity<Asset> checkIn(
            @PathVariable String tag,
            @RequestBody @Validated CheckInRequest req) {

        Asset asset = service.checkIn(tag, req.model());
        return ResponseEntity.created(URI.create("/api/v1/depot/assets/" + asset.getId()))
                .body(asset);
    }

    @Operation(summary = "Check-out an asset")
    @PostMapping("/assets/{tag}/checkout")
    public ResponseEntity<Asset> checkOut(@PathVariable String tag) {
        return ResponseEntity.ok(service.checkOut(tag));
    }

    @Operation(summary = "Request asset transfer to a warehouse")
    @PostMapping("/transfers")
    public ResponseEntity<TransferRequest> transfer(
            @RequestBody @Validated TransferRequestDto req) {

        TransferRequest tr = service.requestTransfer(req.assetTag(), req.destWarehouseCode());
        return ResponseEntity.accepted().body(tr);
    }
}