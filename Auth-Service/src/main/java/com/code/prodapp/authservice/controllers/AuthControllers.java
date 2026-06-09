package com.code.prodapp.authservice.controllers;

import com.code.prodapp.authservice.DTOs.LoginRequestDTO;
import com.code.prodapp.authservice.DTOs.LoginResponseDTO;
import com.code.prodapp.authservice.DTOs.SignupRequestDTO;
import com.code.prodapp.authservice.DTOs.SignupResponseDTO;
import com.code.prodapp.authservice.exceptions.TokenException;
import com.code.prodapp.authservice.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllers {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(authService.signup(signupRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO,
                                                  HttpServletResponse response) {
       LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
       // Creating a Cookie
        Cookie cookie = new Cookie("RefreshToken", loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // Spring Merges the Explicit Response with the cookie and ResponseEntity Body and returns a single response.
       // Every subsequent request will contain this cookie.
       return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] requestCookies = request.getCookies();
        if (requestCookies == null) {
            throw new TokenException("Refresh token cookie is missing, You might need to login again");
        }

        for (Cookie requestCookie : requestCookies) {
            if (requestCookie.getName().equals("RefreshToken")) {
                refreshToken = requestCookie.getValue();
                break;
            }
        }

        return ResponseEntity.ok(authService.refreshAccessToken(refreshToken));

    }




}
