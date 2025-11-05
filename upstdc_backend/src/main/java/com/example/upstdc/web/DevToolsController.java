package com.example.upstdc.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * PUBLIC_INTERFACE
 * Development helpers (can be removed for production).
 */
@RestController
@RequestMapping("/api/dev")
@Tag(name = "Reports")
public class DevToolsController {

    private final PasswordEncoder encoder;
    public DevToolsController(PasswordEncoder encoder){ this.encoder = encoder; }

    @GetMapping("/bcrypt")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate bcrypt hash", description = "Utility for generating bcrypt hashes for testing.")
    public Map<String,String> bcrypt(@RequestParam String raw){
        return Map.of("hash", encoder.encode(raw));
    }
}
