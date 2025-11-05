package com.example.upstdc.web;

import com.example.upstdc.security.JwtService;
import com.example.upstdc.web.dto.AuthDtos.LoginRequest;
import com.example.upstdc.web.dto.AuthDtos.TokenResponse;
import com.example.upstdc.web.dto.AuthDtos.RefreshRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling authentication (login and token refresh).
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService){
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    // PUBLIC_INTERFACE
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with username and password to receive access and refresh tokens.")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username, request.password)
        );
        String username = auth.getName();
        String access = jwtService.generateAccessToken(username, java.util.Map.of("sub", username));
        String refresh = jwtService.generateRefreshToken(username);
        return ResponseEntity.ok(new TokenResponse(access, refresh));
    }

    // PUBLIC_INTERFACE
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Use a valid refresh token to obtain a new access token.")
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshRequest request){
        String username = jwtService.extractUsername(request.refreshToken);
        // In a full system we would track refresh tokens; here we validate signature/expiry.
        String access = jwtService.generateAccessToken(username, java.util.Map.of("sub", username));
        return ResponseEntity.ok(new TokenResponse(access, request.refreshToken));
    }
}
