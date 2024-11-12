package com.example.sportPlanner.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${user-service.app.jwtAccessTokenSecret}")
    private String jwtAccessTokenSecret;
    public boolean validateJwtToken(String authToken) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
            Date expirationDate = Jwts.parser()
                    .setSigningKey(secretKeySpec)
                    .parseClaimsJws(authToken)
                    .getBody()
                    .getExpiration();
            return !expirationDate.before(new Date());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
    public String getUsernameFromJwtToken(String token, String jwtSecret) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .parseClaimsJws(token).getBody().getSubject();
    }
}
