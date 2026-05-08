package com.toolvault.depot_ops_service.repository;

import com.toolvault.depot_ops_service.domain.JobAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobAlertRepository extends JpaRepository<JobAlert, Long> {

    List<JobAlert> findByResolvedFalse();

    List<JobAlert> findByResolvedFalseOrderByCreatedAtDesc();

    List<JobAlert> findByJob_IdAndResolvedFalse(Long jobId);

    long countByResolvedFalse();
}