package com.code.prodapp.apigateway.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JWTCheckerService {

    @Value("${jwt.SecretKey}")
    private String key ;

    private SecretKey HMACGeneratedKey(){
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    // This method verifies JWT and also returns the userId from token of the User.
    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(HMACGeneratedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }






}
