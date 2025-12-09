package com.toolvault.depot_ops_service.repository;

import com.toolvault.depot_ops_service.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByTag(String tag);
    boolean existsByTag(String tag);
}
