package com.keepyuppy.KeepyUppy.security.jwt;

import com.keepyuppy.KeepyUppy.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.access-secret}")
    private String accessSecret;
    private Key accessKey;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;
    private Key refreshKey;

    private static final long accessExp = 1000L * 60 * 30; // 30 minutes
    private static final long refreshExp = 1000L * 60 * 60 * 24 * 7 ; // 7 days
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    protected void init() {
        accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String oauthId) {
        Claims claims = Jwts.claims().setSubject(oauthId);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExp))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExp))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }

    public boolean verifyToken(String token, boolean isAccessToken) {
        Key key = isAccessToken ? accessKey : refreshKey;
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String oauthId = this.parseClaims(token);
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(oauthId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
