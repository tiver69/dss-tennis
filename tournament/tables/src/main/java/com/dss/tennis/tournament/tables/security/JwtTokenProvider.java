package com.dss.tennis.tournament.tables.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    public static final long EXPIRATION_TIME_SECONDS = 5_400; //~ 1.5 hour = 90 min * 60
    public static final long REFRESH_TOKEN_EXPIRATION_HOURS = 6;
    public static final String SECRET_KEY = "SecretKyeToGenerateJWTs";
    public static final String USERNAME_CLAIM = "username";
    public static final String REFRESH_REQUEST_PATH = "/auth/refresh";

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String AUTHENTICATION_FAIL_LOG_MESSAGE = "AUTHENTICATION FAILED | Reason: ";

    @Autowired
    private AdminDetailsService adminDetailsService;

    public String generateToken(Authentication authentication) {
        Admin admin = (Admin) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_SECONDS * 1000);
        String username = admin.getUsername();
        adminDetailsService
                .updateRefreshToken(username, LocalDateTime.now().plusHours(REFRESH_TOKEN_EXPIRATION_HOURS));

        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME_CLAIM, username);
        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException ex) {
            LOGGER.error(AUTHENTICATION_FAIL_LOG_MESSAGE + "invalid token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error(AUTHENTICATION_FAIL_LOG_MESSAGE + "expired token");
            return true;
        } catch (UnsupportedJwtException ex) {
            LOGGER.error(AUTHENTICATION_FAIL_LOG_MESSAGE + "unsupported token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error(AUTHENTICATION_FAIL_LOG_MESSAGE + "token is empty");
        }
        return false;
    }

    public String getAdminUsernameFromJWT(String token, String requestPath) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            return (String) claims.get(USERNAME_CLAIM);
        } catch (ExpiredJwtException ex) {
            if (REFRESH_REQUEST_PATH.equals(requestPath)) return (String) ex.getClaims().get(USERNAME_CLAIM);
            throw ex;
        }
    }
}
