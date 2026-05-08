package com.toolvault.depot_ops_service.repository;

import com.toolvault.depot_ops_service.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByTechnicianEmail(String technicianEmail);

    List<Job> findByStatus(Job.Status status);

    List<Job> findByTechnicianEmailAndStatus(String technicianEmail, Job.Status status);

    List<Job> findByStatusAndReturnDeadlineBefore(Job.Status status, Instant deadline);
}