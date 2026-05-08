package com.toolvault.depot_ops_service.service;

import com.toolvault.depot_ops_service.domain.Asset;
import com.toolvault.depot_ops_service.domain.Asset.Status;
import com.toolvault.depot_ops_service.domain.CheckoutHistory;
import com.toolvault.depot_ops_service.domain.TransferRequest;
import com.toolvault.depot_ops_service.domain.TransferRequest.State;
import com.toolvault.depot_ops_service.exception.AssetNotFoundException;
import com.toolvault.depot_ops_service.exception.InvalidAssetStateException;
import com.toolvault.depot_ops_service.exception.InvalidTagException;
import com.toolvault.depot_ops_service.integration.WarehouseClient;
import com.toolvault.depot_ops_service.repository.AssetRepository;
import com.toolvault.depot_ops_service.repository.CheckoutHistoryRepository;
import com.toolvault.depot_ops_service.repository.TransferRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepotService {

    private final AssetRepository assets;
    private final TransferRequestRepository transfers;
    private final WarehouseClient warehouseClient;
    private final CheckoutHistoryRepository history;

    public List<Asset> listAssets() {
        return assets.findAll();
    }

    @Transactional
    public Asset checkIn(String tag, String model) {
        validateTag(tag);

        Asset asset = assets.findByTag(tag)
                .orElse(new Asset(tag, model, Status.IN_DEPOT));

        if (model != null && !model.isBlank()) {
            asset.setModel(model);
        }

        asset.setStatus(Status.IN_DEPOT);
        Asset saved = assets.save(asset);

        logCheckoutHistory(tag, "CHECK_IN");

        try {
            warehouseClient.restock(tag, 1, model);
        } catch (Exception e) {
            log.warn("Warehouse restock failed for tag {}: {}", tag, e.getMessage());
        }

        return saved;
    }

    @Transactional
    public Asset checkOut(String tag) {
        validateTag(tag);

        Asset asset = assets.findByTag(tag)
                .orElseThrow(() -> new AssetNotFoundException(tag));

        if (asset.getStatus() == Status.OUT_WITH_TECH) {
            throw new InvalidAssetStateException("Asset already checked out");
        }

        asset.setStatus(Status.OUT_WITH_TECH);
        Asset saved = assets.save(asset);

        logCheckoutHistory(tag, "CHECK_OUT");

        try {
            warehouseClient.allocate(tag, 1);
        } catch (Exception e) {
            log.warn("Warehouse allocate failed for tag {}: {}", tag, e.getMessage());
        }

        return saved;
    }

    @Transactional
    public TransferRequest requestTransfer(String tag, String destWarehouse) {
        validateTag(tag);

        Asset asset = assets.findByTag(tag)
                .orElseThrow(() -> new AssetNotFoundException(tag));

        if (asset.getStatus() == Status.OUT_WITH_TECH) {
            throw new InvalidAssetStateException("Cannot transfer checked-out asset");
        }

        asset.setStatus(Status.IN_TRANSIT);
        assets.save(asset);

        TransferRequest tr = new TransferRequest(tag, destWarehouse, State.REQUESTED);
        TransferRequest saved = transfers.save(tr);

        logCheckoutHistory(tag, "TRANSFER_REQUESTED");

        return saved;
    }

    private void validateTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new InvalidTagException("Tag cannot be empty");
        }
    }

    private void logCheckoutHistory(String tag, String action) {
        history.save(new CheckoutHistory(tag, action));
    }
}