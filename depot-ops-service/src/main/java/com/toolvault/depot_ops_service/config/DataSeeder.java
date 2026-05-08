package com.toolvault.depot_ops_service.config;

import com.toolvault.depot_ops_service.domain.Asset;
import com.toolvault.depot_ops_service.repository.AssetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Profile("!test")
public class DataSeeder {
    private final AssetRepository assets;

    public DataSeeder(AssetRepository assets) {
        this.assets = assets;
    }

    @PostConstruct
    public void seed() {
        if (assets.count() == 0) {
            Asset a1 = new Asset("ASSET-1001", "Hilti TE 7-C", Asset.Status.IN_DEPOT);
            Asset a2 = new Asset("ASSET-1002", "DeWalt DCD996", Asset.Status.IN_DEPOT);
            assets.save(a1);
            assets.save(a2);
        }
    }
}