package com.toolvault.depot_ops_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "checkout_history")
public class CheckoutHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String assetTag;

    @NotBlank
    @Column(nullable = false)
    private String action; // CHECK_IN, CHECK_OUT, TRANSFER_REQUESTED, etc.

    private String model; // optional but useful for analytics

    private String site; // optional for multi-site forecasting

    @CreationTimestamp
    private Instant timestamp;

    protected CheckoutHistory() {}

    public CheckoutHistory(String assetTag, String action) {
        this.assetTag = assetTag;
        this.action = action;
    }

    public Long getId() { return id; }
    public String getAssetTag() { return assetTag; }
    public String getAction() { return action; }
    public String getModel() { return model; }
    public String getSite() { return site; }
    public Instant getTimestamp() { return timestamp; }

    public void setModel(String model) { this.model = model; }
    public void setSite(String site) { this.site = site; }

    @Override
    public String toString() {
        return "CheckoutHistory{" +
                "id=" + id +
                ", assetTag='" + assetTag + '\'' +
                ", action='" + action + '\'' +
                ", model='" + model + '\'' +
                ", site='" + site + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}