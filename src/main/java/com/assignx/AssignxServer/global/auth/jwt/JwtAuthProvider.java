package com.assignx.AssignxServer.global.auth.jwt;

import com.assignx.AssignxServer.domain.member.entity.Role;
import com.assignx.AssignxServer.global.auth.exception.AuthExceptionUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

    public String generateAccessToken(String idNumber, Role role) {
        return Jwts.builder()
                .claim("idNumber", idNumber)
                .claim("role", role)
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

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw AuthExceptionUtils.TokenExpired();
        } catch (JwtException | IllegalArgumentException e) {
            throw AuthExceptionUtils.AccessTokenInvalid();
        }
    }
}
