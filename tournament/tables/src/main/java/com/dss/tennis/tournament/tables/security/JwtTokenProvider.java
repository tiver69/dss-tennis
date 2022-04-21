package com.dss.tennis.tournament.tables.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    public static final long EXPIRATION_TIME = 900_000; //~ 15 min = 60 * 15
    public static final String SECRET_KEY = "SecretKyeToGenerateJWTs";
    public static final String USERNAME_CLAIM = "username";

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String AUTHENTICATION_FAIL_LOG_MESSAGE = "AUTHENTICATION FAILED | Reason: ";

    public String generateToken(Authentication authentication) {
        Admin admin = (Admin) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String username = admin.getUsername();
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
        } catch (UnsupportedJwtException ex) {
            LOGGER.error(AUTHENTICATION_FAIL_LOG_MESSAGE + "unsupported token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error(AUTHENTICATION_FAIL_LOG_MESSAGE + "token is empty");
        }
        return false;
    }

    public String getAdminUsernameFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return (String) claims.get(USERNAME_CLAIM);
    }
}
