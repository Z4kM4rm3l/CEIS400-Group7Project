package com.toolvault.depot_ops_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String tag;

    @Size(max = 255)
    private String model;

    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_DEPOT;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public enum Status { IN_DEPOT, OUT_WITH_TECH, IN_TRANSIT }

    protected Asset() {}

    public Asset(String tag, String model, Status status) {
        this.tag = tag;
        this.model = model;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getTag() { return tag; }
    public String getModel() { return model; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setTag(String tag) { this.tag = tag; }
    public void setModel(String model) { this.model = model; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", model='" + model + '\'' +
                ", status=" + status +
                '}';
    }
}