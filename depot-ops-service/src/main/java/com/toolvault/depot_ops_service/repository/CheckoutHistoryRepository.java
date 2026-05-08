package com.toolvault.depot_ops_service.repository;

import com.toolvault.depot_ops_service.domain.CheckoutHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface CheckoutHistoryRepository extends JpaRepository<CheckoutHistory, Long> {
    List<CheckoutHistory> findByAssetTag(String assetTag);
    List<CheckoutHistory> findByAction(String action);
    List<CheckoutHistory> findByActionAndTimestampAfter(String action, Instant since);
}