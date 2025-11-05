package com.example.upstdc.web;

import com.example.upstdc.model.Document;
import com.example.upstdc.model.Project;
import com.example.upstdc.repository.DocumentRepository;
import com.example.upstdc.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Endpoints for uploading and downloading project documents.
 */
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents")
public class DocumentController {

    private final DocumentRepository documents;
    private final ProjectRepository projects;

    @Value("${app.storage.base-path}")
    private String basePath;

    public DocumentController(DocumentRepository documents, ProjectRepository projects){
        this.documents = documents;
        this.projects = projects;
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "List documents for a project")
    public List<Document> listForProject(@PathVariable Long projectId){
        return documents.findByProjectId(projectId);
    }

    @PostMapping(value = "/project/{projectId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Upload a document for a project")
    public ResponseEntity<Document> upload(@PathVariable Long projectId, @RequestPart("file") MultipartFile file) throws Exception {
        Project project = projects.findById(projectId).orElse(null);
        if (project == null) return ResponseEntity.notFound().build();
        Path dir = Path.of(basePath, "projects", String.valueOf(projectId));
        Files.createDirectories(dir);
        Path target = dir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), target);

        Document doc = new Document();
        doc.setProject(project);
        doc.setFilename(file.getOriginalFilename());
        doc.setContentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
        doc.setSizeBytes(file.getSize());
        doc.setStoragePath(target.toAbsolutePath().toString());
        documents.save(doc);
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Download a document")
    public ResponseEntity<FileSystemResource> download(@PathVariable Long id) throws Exception {
        Document doc = documents.findById(id).orElse(null);
        if (doc == null) return ResponseEntity.notFound().build();
        File file = new File(doc.getStoragePath());
        if (!file.exists()) return ResponseEntity.notFound().build();
        FileSystemResource resource = new FileSystemResource(file);
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        try {
            String probe = Files.probeContentType(file.toPath());
            if (probe != null) mediaType = MediaType.parseMediaType(probe);
        } catch (Exception ignored){}
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFilename() + "\"")
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a document")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return documents.findById(id).map(doc -> {
            FileSystemUtils.deleteRecursively(new File(doc.getStoragePath()));
            documents.delete(doc);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
