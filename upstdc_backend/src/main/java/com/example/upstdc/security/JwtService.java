package com.example.upstdc.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * PUBLIC_INTERFACE
 * Service for creating and validating JWT access & refresh tokens.
 */
@Service
public class JwtService {

    @Value("${app.security.jwt.secret}")
    private String secret;

    @Value("${app.security.jwt.expiration-ms}")
    private long accessExpirationMs;

    @Value("${app.security.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // PUBLIC_INTERFACE
    public String generateAccessToken(String username, Map<String, Object> claims){
        return buildToken(username, claims, accessExpirationMs);
    }

    // PUBLIC_INTERFACE
    public String generateRefreshToken(String username){
        return buildToken(username, Map.of("type","refresh"), refreshExpirationMs);
    }

    private String buildToken(String username, Map<String,Object> claims, long expiryMs){
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiryMs);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // PUBLIC_INTERFACE
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // PUBLIC_INTERFACE
    public boolean isTokenValid(String token, String username){
        final String subject = extractUsername(token);
        return username.equals(subject) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }
}
