package com.example.upstdc.repository;

import com.example.upstdc.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Repository for Document.
 */
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByProjectId(Long projectId);
}
