package com.Spring.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSigningKey() {
        return secretKey;
    }

    // 
    
        public String generateToken(String username) {
            String token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutes
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
            
            // Debugging output
            System.out.println("Generated JWT Token: " + token);
            System.out.println("Authenticated User: " + username);
            
            return token;
        }

    // 
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    // 
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ✅ Extract Claims
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

 
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    
}
