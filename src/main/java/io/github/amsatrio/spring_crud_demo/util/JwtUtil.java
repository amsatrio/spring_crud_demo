package io.github.amsatrio.spring_crud_demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    @Value("${app.auth.token_secret}")
    private String secret;

    @Value("${app.auth.main_token_expiration_in_ms}")
    private Long mainTokenExpiration;
    @Value("${app.auth.refresh_token_expiration_in_ms}")
    private Long refreshTokenExpiration;

    private Claims claims;

    public Long getExpiration(){
        return mainTokenExpiration;
    }

    public String reGenerateToken() {
        long systemMillis = System.currentTimeMillis();
        Date now = new Date(systemMillis);
        Date expirationDate = new Date(systemMillis + (mainTokenExpiration));

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", this.claims.getOrDefault("authorities", new ArrayList<>()));
        claims.put("isAccountNonExpired", true);
        claims.put("isAccountNonLocked", true);
        claims.put("isCredentialsNonExpired", true);
        claims.put("isEnabled", true);
        claims.put("tokenType", "MAIN_TOKEN");

        return Jwts.builder()
                .claims(claims)
                .subject(this.claims.getSubject())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public String generateToken(UserDetails userDetails, boolean isMainToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", getAuthorities(userDetails.getAuthorities()));
        claims.put("isAccountNonExpired", true);
        claims.put("isAccountNonLocked", true);
        claims.put("isCredentialsNonExpired", true);
        claims.put("isEnabled", true);
        claims.put("tokenType", "MAIN_TOKEN");
        if(!isMainToken){
            claims.put("tokenType", "REFRESH_TOKEN");
            return createRefreshToken(claims, userDetails.getUsername());
        }
        return createToken(claims, userDetails.getUsername());
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        long systemMillis = System.currentTimeMillis();
        Date now = new Date(systemMillis);
        Date expirationDate = new Date(systemMillis + refreshTokenExpiration);

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long systemMillis = System.currentTimeMillis();
        Date now = new Date(systemMillis);
        Date expirationDate = new Date(systemMillis + mainTokenExpiration);

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            log.error("error: ", e.getMessage());
        }

        return false;
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        extractAllClaims(token);
        return claimsResolver.apply(this.claims);
    }

    private void extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        try {
            this.claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            this.claims =  e.getClaims();
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw e;
        }
    }

    public Boolean isExpired() {
        long systemMillis = System.currentTimeMillis();
        Date now = new Date(systemMillis);
        final Date expirationDate = this.claims.getExpiration();
        // return expirationDate.getTime() < now.getTime();
        return expirationDate.before(now);
    }

    private Collection<String> getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream().parallel()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public String getUsername() {
        return this.claims.getSubject();
    }

    public String getTokenType() {
        return this.claims.get("tokenType").toString();
    }

}
