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

    public DataSeeder(AssetRepository assets) { this.assets = assets; }

    @PostConstruct
    public void seed() {
        if (assets.count() == 0) {
            Asset a1 = new Asset();
            a1.setTag("ASSET-1001");
            a1.setModel("Hilti TE 7-C");
            a1.setStatus(Asset.Status.IN_DEPOT);

            Asset a2 = new Asset();
            a2.setTag("ASSET-1002");
            a2.setModel("DeWalt DCD996");
            a2.setStatus(Asset.Status.IN_DEPOT);

            assets.save(a1);
            assets.save(a2);
        }
    }
}
