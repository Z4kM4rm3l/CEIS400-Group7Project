package com.toolvault.depot_ops_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "transfer_requests")
public class TransferRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String assetTag;

    @NotBlank
    @Column(nullable = false)
    private String destWarehouseCode;

    @Enumerated(EnumType.STRING)
    private State state = State.REQUESTED;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public enum State { REQUESTED, IN_TRANSIT, COMPLETED }

    protected TransferRequest() {}

    public TransferRequest(String assetTag, String destWarehouseCode, State state) {
        this.assetTag = assetTag;
        this.destWarehouseCode = destWarehouseCode;
        this.state = state;
    }

    public Long getId() { return id; }
    public String getAssetTag() { return assetTag; }
    public String getDestWarehouseCode() { return destWarehouseCode; }
    public State getState() { return state; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }
    public void setDestWarehouseCode(String destWarehouseCode) { this.destWarehouseCode = destWarehouseCode; }
    public void setState(State state) { this.state = state; }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "id=" + id +
                ", assetTag='" + assetTag + '\'' +
                ", destWarehouseCode='" + destWarehouseCode + '\'' +
                ", state=" + state +
                '}';
    }
}