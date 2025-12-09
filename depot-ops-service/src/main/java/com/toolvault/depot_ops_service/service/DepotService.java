package com.toolvault.depot_ops_service.service;

import com.toolvault.depot_ops_service.domain.Asset;
import com.toolvault.depot_ops_service.domain.TransferRequest;
import com.toolvault.depot_ops_service.integration.WarehouseClient;
import com.toolvault.depot_ops_service.repository.AssetRepository;
import com.toolvault.depot_ops_service.repository.TransferRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepotService {

    private final AssetRepository assets;
    private final TransferRequestRepository transfers;
    private final WarehouseClient warehouseClient;

    public DepotService(AssetRepository assets,
                        TransferRequestRepository transfers,
                        WarehouseClient warehouseClient) {
        this.assets = assets;
        this.transfers = transfers;
        this.warehouseClient = warehouseClient;
    }

    public List<Asset> listAssets() {
        return assets.findAll();
    }

    @Transactional
    public Asset checkIn(String tag, String model) {
        Asset asset = assets.findByTag(tag).orElseGet(Asset::new);
        asset.setTag(tag);
        if (model != null && !model.isBlank()) asset.setModel(model);
        asset.setStatus(Asset.Status.IN_DEPOT);
        Asset saved = assets.save(asset);

        // Cross-service: restock in Warehouse
        try {
            warehouseClient.restock(tag, 1, model);
        } catch (Exception ignore) { /* demo-safe */ }

        return saved;
    }

    @Transactional
    public Asset checkOut(String tag) {
        Asset asset = assets.findByTag(tag)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + tag));
        asset.setStatus(Asset.Status.OUT_WITH_TECH);
        Asset saved = assets.save(asset);

        // Cross-service: allocate in Warehouse
        try {
            warehouseClient.allocate(tag, 1);
        } catch (Exception ignore) { /* demo-safe */ }

        return saved;
    }

    @Transactional
    public TransferRequest requestTransfer(String assetTag, String destWarehouseCode) {
        Asset asset = assets.findByTag(assetTag)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + assetTag));
        asset.setStatus(Asset.Status.IN_TRANSIT);
        assets.save(asset);

        TransferRequest tr = new TransferRequest();
        tr.setAssetTag(assetTag);
        tr.setDestWarehouseCode(destWarehouseCode);
        tr.setState(TransferRequest.State.REQUESTED);
        return transfers.save(tr);
    }
}
