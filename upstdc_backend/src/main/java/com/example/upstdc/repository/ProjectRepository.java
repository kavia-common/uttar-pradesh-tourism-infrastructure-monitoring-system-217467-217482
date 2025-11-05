package com.example.upstdc.repository;

import com.example.upstdc.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PUBLIC_INTERFACE
 * Repository for Project.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
