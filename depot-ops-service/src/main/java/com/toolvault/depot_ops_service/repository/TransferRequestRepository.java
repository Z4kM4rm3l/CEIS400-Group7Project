package com.toolvault.depot_ops_service.repository;

import com.toolvault.depot_ops_service.domain.TransferRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRequestRepository extends JpaRepository<TransferRequest, Long> { }
