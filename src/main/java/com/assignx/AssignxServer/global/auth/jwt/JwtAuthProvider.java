package com.assignx.AssignxServer.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access-expiration-ms}")
    private long accessTokenValidity;
    @Value("${jwt.refresh-expiration-ms}")
    private long refreshTokenValidity;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String idNumber) {
        return Jwts.builder()
                .claim("idNumber", idNumber)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(this.getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String idNumber) {
        return Jwts.builder()
                .claim("idNumber", idNumber)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(this.getSigningKey())
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getMemberIdNumber(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("idNumber").toString();
    }

}
