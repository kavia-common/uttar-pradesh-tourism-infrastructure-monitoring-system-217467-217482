package com.example.upstdc.model;

import jakarta.persistence.*;

@Entity
@Table(name="projects")
public class Project extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String location;
    private String status;

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public String getLocation(){ return location; }
    public void setLocation(String location){ this.location = location; }
    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }
}
