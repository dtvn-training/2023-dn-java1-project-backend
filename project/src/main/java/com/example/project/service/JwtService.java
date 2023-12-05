package com.example.project.service;

import com.example.project.model.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
@Service
public class JwtService {

    @Autowired
    IUserService userService;

    @Autowired
    private MessageSource messageSource;

    @Value("${app.jwt.secret_key}")
    private String SECRET_KEY;

    @Value("${app.jwt.tokenExpirationMs}")
    private long EXPIRE_TIME_TOKEN;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class.getName());

    public String generateAccessToken(Authentication authentication) {
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME_TOKEN))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME_TOKEN))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("{} -> Message: {} ",
                    messageSource.getMessage("invalid.jwt.signature", null, LocaleContextHolder.getLocale()), e);
        } catch (MalformedJwtException e) {
            logger.error("{} -> Message: {} ",
                    messageSource.getMessage("invalid.jwt.token", null, LocaleContextHolder.getLocale()), e);
        } catch (ExpiredJwtException e) {
            logger.error("{} -> Message: {} ",
                    messageSource.getMessage("expired.jwt.token", null, LocaleContextHolder.getLocale()), e);
        } catch (UnsupportedJwtException e) {
            logger.error("{} -> Message: {} ",
                    messageSource.getMessage("unsupported.jwt.token", null, LocaleContextHolder.getLocale()), e);
        } catch (IllegalArgumentException e) {
            logger.error("{} -> Message: {} ",
                    messageSource.getMessage("empty.jwt.claims", null, LocaleContextHolder.getLocale()), e);
        }
        return false;
    }

    public String getEmailFromJwtToken(String token) {
        String email = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return email;
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
}
