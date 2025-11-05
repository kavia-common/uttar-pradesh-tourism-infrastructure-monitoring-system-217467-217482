package com.example.upstdc.repository;

import com.example.upstdc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * PUBLIC_INTERFACE
 * Repository for Role.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
