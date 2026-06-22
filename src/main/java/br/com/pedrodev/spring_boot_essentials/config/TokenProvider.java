package br.com.pedrodev.spring_boot_essentials.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenProvider {
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.key}")
    private String key;

    //gerar um token
    public String generateToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return buildToken(user.getUsername());
    }

    private String buildToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder().subject(username).issuedAt(now).expiration(expiryDate).signWith(getSigningKey()).compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    //validar um token
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }
    //extrair username do token
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }
}
