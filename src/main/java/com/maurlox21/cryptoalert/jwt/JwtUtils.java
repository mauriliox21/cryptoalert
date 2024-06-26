package com.maurlox21.cryptoalert.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {

    @Value("environment.jwt.secret.key")
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789";
    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final long EXPIRE_DAYS = 30;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 0;

    public static JwtToken createToken(String email, String role) {
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);

        String token = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(email)
            .setIssuedAt(issuedAt)
            .setExpiration(limit)
            .signWith(generateKey(), SignatureAlgorithm.HS256)
            .claim("role", role)
            .compact();

        return new JwtToken(token);
    }

    private static Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean isTokenValid(String token) {
        try{
            Jwts.parserBuilder()
                .setSigningKey(generateKey()).build()
                .parseClaimsJws(refactorToken(token));
            
            return true;
            
        } catch(JwtException ex){
            System.out.println("Token invaldo");
        }

        return false;
    }

    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    private static Claims getClaimsFromToken(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();
        }catch(JwtException ex){
            System.out.println("Token invaldo");
        }

        return null;
    }

}
