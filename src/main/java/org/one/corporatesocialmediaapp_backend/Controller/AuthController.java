package org.one.corporatesocialmediaapp_backend.Controller;


import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.UserLoginRequest;
import org.one.corporatesocialmediaapp_backend.Models.CustomUserDetails;
import org.one.corporatesocialmediaapp_backend.Service.AuthService.JWTService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.util.Map;

@Repository
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    final AuthenticationManager authenticationManager;
    final JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        String jwt = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Login successful"));
    }

}
