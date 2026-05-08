package com.toolvault.depot_ops_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String site;

    @NotBlank
    @Column(nullable = false)
    private String technicianEmail;

    @NotBlank
    @Column(nullable = false)
    private String technicianName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "job_assets", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "asset_tag")
    private List<String> assetTags = new ArrayList<>();

    @CreationTimestamp
    private Instant createdAt;

    private Instant completedAt;

    private Instant returnDeadline;

    public enum Status {
        ACTIVE,
        COMPLETED,
        OVERDUE
    }

    protected Job() {}

    public Job(String name, String site, String technicianEmail, String technicianName) {
        this.name = name;
        this.site = site;
        this.technicianEmail = technicianEmail;
        this.technicianName = technicianName;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSite() { return site; }
    public String getTechnicianEmail() { return technicianEmail; }
    public String getTechnicianName() { return technicianName; }
    public Status getStatus() { return status; }
    public List<String> getAssetTags() { return assetTags; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }
    public Instant getReturnDeadline() { return returnDeadline; }

    public void setStatus(Status status) { this.status = status; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public void setReturnDeadline(Instant returnDeadline) { this.returnDeadline = returnDeadline; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }
    public void setTechnicianEmail(String technicianEmail) { this.technicianEmail = technicianEmail; }
    public void addAsset(String tag) { this.assetTags.add(tag); }
    public void removeAsset(String tag) { this.assetTags.remove(tag); }
}