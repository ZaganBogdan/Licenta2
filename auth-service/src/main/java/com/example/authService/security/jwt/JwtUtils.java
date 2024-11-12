package com.example.authService.security.jwt;

import com.example.authService.entities.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${sportPlanner.app.jwtAccessTokenSecret}")
    private String jwtAccessTokenSecret;

    @Value("${sportPlanner.app.jwtRefreshTokenSecret}")
    private String jwtRefreshTokenSecret;

    @Value("${sportPlanner.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private String generateToken(User user, String jwtAccessTokenSecret) {
        return Jwts.builder()
                .setSubject((user.getUsername()))
                .claim("authorities", Arrays.asList(user.getType().name()))
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String generateRefreshJwtToken(User user) {
        return generateToken(user, jwtRefreshTokenSecret);
    }

    public String generateAccessJwtToken(User user) {
        return generateToken(user, jwtAccessTokenSecret);
    }

    public String getUsernameFromJwtToken(String token, String jwtSecret) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getUsernameFromJwtAccessToken(String token) {
        return getUsernameFromJwtToken(token, jwtAccessTokenSecret);
    }

    public String getUsernameFromJwtRefreshToken(String token) {
        return getUsernameFromJwtToken(token, jwtRefreshTokenSecret);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
//            Date expirationDate = Jwts.parser()
//                    .setSigningKey(Base64.getEncoder()
//                            .encodeToString(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8)))
//                    .parseClaimsJws(authToken)
//                    .getBody().getExpiration();
            Date expirationDate = Jwts.parser()
                    .setSigningKey(secretKeySpec)
                    .parseClaimsJws(authToken)
                    .getBody()
                    .getExpiration();
            return !expirationDate.before(new Date());
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}