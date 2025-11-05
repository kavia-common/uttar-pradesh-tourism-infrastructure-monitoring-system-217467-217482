package com.example.upstdc.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
        joinColumns = @JoinColumn(name="role_id"),
        inverseJoinColumns = @JoinColumn(name="permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public Set<Permission> getPermissions(){ return permissions; }
    public void setPermissions(Set<Permission> permissions){ this.permissions = permissions; }
}
