package com.example.upstdc.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

    public static class LoginRequest {
        @Schema(description = "Username", example = "admin")
        @NotBlank public String username;
        @Schema(description = "Password", example = "password")
        @NotBlank public String password;
    }

    public static class TokenResponse {
        @Schema(description = "JWT access token") public String accessToken;
        @Schema(description = "JWT refresh token") public String refreshToken;
        public TokenResponse(String a, String r){ this.accessToken=a; this.refreshToken=r; }
    }

    public static class RefreshRequest {
        @Schema(description = "Refresh token") @NotBlank public String refreshToken;
    }
}
