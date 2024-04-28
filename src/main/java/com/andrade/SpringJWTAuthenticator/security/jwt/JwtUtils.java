package com.andrade.SpringJWTAuthenticator.security.jwt;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;

import com.andrade.SpringJWTAuthenticator.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class JwtUtils {

  @Value("${project.jwtSecret}")//chave de segurança definida nas proproedades do projeto.
  private String jwtSecret;

  @Value("${project.jwtExpirationMs}")
  private int jwtExpirationMs;//tempo de expiração do "token", definido nas propriedades do projeto.

  public String generateTokenFromUserDetailsImpl(UserDetailsImpl userDetails) {
    return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSigningKey())
            .compact();
  }

  public String extractUsernameFromJwtToken(String token) {
    return extractClaimFromJwtToken(token, Claims::getSubject);
  }
  public boolean validateJwtToken(String token) {
    try {
      Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("Token expirado", e);
    } catch (UnsupportedJwtException e) {
      throw new RuntimeException("Token não suportado", e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Token vazio", e);
    } catch (MalformedJwtException e) {
      throw new RuntimeException("Token inválido", e);
    }
  }
  private <T> T extractClaimFromJwtToken(String token, Function<Claims, T> claimsResolver) {

    Claims claims = Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    return claimsResolver.apply(claims);
  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

}

