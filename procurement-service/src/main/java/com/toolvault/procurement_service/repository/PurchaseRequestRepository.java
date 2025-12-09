package com.toolvault.procurement_service.repository;

import com.toolvault.procurement_service.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {}
