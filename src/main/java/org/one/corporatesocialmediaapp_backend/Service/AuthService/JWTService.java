package org.one.corporatesocialmediaapp_backend.Service.AuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.one.corporatesocialmediaapp_backend.Exceptions.AuthExceptions.AuthTokenExpiredException;
import org.one.corporatesocialmediaapp_backend.Exceptions.AuthExceptions.AuthTokenInvalidException;
import org.one.corporatesocialmediaapp_backend.Models.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(CustomUserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())  // optional: include id in token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthTokenExpiredException("JWT token has expired");
        } catch (JwtException e) {
            throw new AuthTokenInvalidException("Invalid JWT token");
        }
    }

    public String extractUsername(String token) {
        try {
            return extractAllClaims(token).getSubject();
        } catch (ExpiredJwtException e) {
            throw new AuthTokenExpiredException("JWT token has expired");
        } catch (JwtException e) {
            throw new AuthTokenInvalidException("Invalid JWT token");
        }
    }

    public boolean isTokenValid(String token, CustomUserDetails user) {
        try {
            return extractUsername(token).equals(user.getUsername())
                    && !extractAllClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new AuthTokenExpiredException("JWT token has expired");
        } catch (JwtException e) {
            throw new AuthTokenInvalidException("Invalid JWT token");
        }
    }
}
