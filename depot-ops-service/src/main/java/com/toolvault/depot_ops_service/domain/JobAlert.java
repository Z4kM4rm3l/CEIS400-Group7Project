package com.toolvault.depot_ops_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "job_alerts")
public class JobAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType type;

    @NotBlank
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean resolved = false;

    @CreationTimestamp
    private Instant createdAt;

    private Instant resolvedAt;

    public enum AlertType {
        OVERDUE_RETURN,
        JOB_COMPLETED,
        MISSING_ASSET
    }

    protected JobAlert() {}

    public JobAlert(Job job, AlertType type, String message) {
        this.job = job;
        this.type = type;
        this.message = message;
    }

    public Long getId() { return id; }

    @JsonIgnore
    public Job getJob() { return job; }

    @JsonProperty("jobId")
    public Long getJobId() {
        return job != null ? job.getId() : null;
    }

    @JsonProperty("jobName")
    public String getJobName() {
        return job != null ? job.getName() : null;
    }

    @JsonProperty("technicianName")
    public String getTechnicianName() {
        return job != null ? job.getTechnicianName() : null;
    }

    @JsonProperty("site")
    public String getSite() {
        return job != null ? job.getSite() : null;
    }

    public AlertType getType() { return type; }
    public String getMessage() { return message; }
    public boolean isResolved() { return resolved; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getResolvedAt() { return resolvedAt; }

    public void resolve() {
        this.resolved = true;
        this.resolvedAt = Instant.now();
    }
}