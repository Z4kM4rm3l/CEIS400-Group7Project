// src/main/java/com/toolvault/identity_service/domain/MainEntity.java
package com.toolvault.identity_service.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "main_entities")
public class MainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
