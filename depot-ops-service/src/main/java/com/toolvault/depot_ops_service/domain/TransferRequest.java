package com.toolvault.depot_ops_service.domain;

import jakarta.persistence.*;

@Entity
public class TransferRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String assetTag;

    @Column(nullable = false)
    private String destWarehouseCode;

    @Enumerated(EnumType.STRING)
    private State state = State.REQUESTED;

    public enum State { REQUESTED, IN_TRANSIT, COMPLETED }

    public Long getId() { return id; }
    public String getAssetTag() { return assetTag; }
    public String getDestWarehouseCode() { return destWarehouseCode; }
    public State getState() { return state; }

    public void setId(Long id) { this.id = id; }
    public void setAssetTag(String assetTag) { this.assetTag = assetTag; }
    public void setDestWarehouseCode(String destWarehouseCode) { this.destWarehouseCode = destWarehouseCode; }
    public void setState(State state) { this.state = state; }
}
