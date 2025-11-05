package com.example.upstdc.repository;

import com.example.upstdc.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PUBLIC_INTERFACE
 * Repository for Permission.
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String name);
}
