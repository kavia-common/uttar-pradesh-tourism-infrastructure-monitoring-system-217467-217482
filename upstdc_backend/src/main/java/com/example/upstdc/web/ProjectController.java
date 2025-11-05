package com.example.upstdc.web;

import com.example.upstdc.model.Project;
import com.example.upstdc.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * CRUD endpoints for Projects.
 */
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects")
public class ProjectController {

    private final ProjectRepository repository;
    public ProjectController(ProjectRepository repository){ this.repository = repository; }

    @GetMapping
    @Operation(summary = "List projects")
    public List<Project> list(){ return repository.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by id")
    public ResponseEntity<Project> get(@PathVariable Long id){
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create project")
    public Project create(@Valid @RequestBody Project p){
        return repository.save(p);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update project")
    public ResponseEntity<Project> update(@PathVariable Long id, @Valid @RequestBody Project p){
        return repository.findById(id).map(existing -> {
            existing.setName(p.getName());
            existing.setLocation(p.getLocation());
            existing.setStatus(p.getStatus());
            return ResponseEntity.ok(repository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete project")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
