package com.example.upstdc.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Base entity with id and auditing timestamps.
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, updatable = false)
    protected Instant createdAt = Instant.now();

    @Column(nullable = false)
    protected Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = Instant.now();
    }

    public Long getId(){ return id; }
    public Instant getCreatedAt(){ return createdAt; }
    public Instant getUpdatedAt(){ return updatedAt; }
}
