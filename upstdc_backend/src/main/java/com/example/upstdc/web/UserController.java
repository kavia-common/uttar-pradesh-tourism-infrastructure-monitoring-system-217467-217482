package com.example.upstdc.web;

import com.example.upstdc.model.User;
import com.example.upstdc.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Minimal user listing endpoint for admin verification.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {

    private final UserRepository userRepository;
    public UserController(UserRepository userRepository){ this.userRepository = userRepository; }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List users")
    public List<User> list(){
        return userRepository.findAll();
    }
}
