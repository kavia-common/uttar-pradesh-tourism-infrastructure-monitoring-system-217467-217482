package com.example.upstdc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
}
