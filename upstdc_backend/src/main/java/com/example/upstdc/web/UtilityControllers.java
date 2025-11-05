package com.example.upstdc.web;

import com.example.upstdc.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * PUBLIC_INTERFACE
 * Health and reporting utilities.
 */
@RestController
public class UtilityControllers {

    private final ProjectRepository projectRepository;
    public UtilityControllers(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @GetMapping("/api/health")
    @Operation(summary = "Health", description = "Basic health indicator for the API service.")
    @Tag(name = "Reports")
    public Map<String,Object> health(){
        return Map.of("status","UP");
    }

    @GetMapping("/api/reports/summary")
    @Operation(summary = "Summary report", description = "Returns basic counts for dashboard.")
    @Tag(name = "Reports")
    public Map<String,Object> summary(){
        long projects = projectRepository.count();
        return Map.of(
                "projects", projects
        );
    }
}
