package com.toolvault.depot_ops_service.domain;

import jakarta.persistence.*;

@Entity
public class Asset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tag;

    private String model;

    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_DEPOT;

    public enum Status { IN_DEPOT, OUT_WITH_TECH, IN_TRANSIT }

    public Long getId() { return id; }
    public String getTag() { return tag; }
    public String getModel() { return model; }
    public Status getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setTag(String tag) { this.tag = tag; }
    public void setModel(String model) { this.model = model; }
    public void setStatus(Status status) { this.status = status; }
}
