package com.andrade.SpringJWTAuthenticator.service;


import com.andrade.SpringJWTAuthenticator.dto.AccessDTO;
import com.andrade.SpringJWTAuthenticator.dto.AuthenticationDTO;
import com.andrade.SpringJWTAuthenticator.model.exceptions.InvalidCredentialsException;
import com.andrade.SpringJWTAuthenticator.security.jwt.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;




@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;

  private final JwtUtils jwtUtils;

  public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  public AccessDTO login(AuthenticationDTO authDto) {

    try {
      //Cria mecanismo de credencial para o Spring
      UsernamePasswordAuthenticationToken userAuth =
              new UsernamePasswordAuthenticationToken(authDto.username(), authDto.password());

      //Prepara mecanismo para Autenticação
      Authentication authentication = authenticationManager.authenticate(userAuth); //retorna erro 2024-04-17T19:54:37.936-03:00  WARN 1092 --- [SkyFeedConnect] [nio-8080-exec-4] o.s.s.c.bcrypt.BCryptPasswordEncoder     : Encoded password does not look like BCrypt

      //Busca usuario logado
      UserDetailsImpl userAuthDetails = (UserDetailsImpl) authentication.getPrincipal();

      String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthDetails);

      return new AccessDTO(token);

    } catch (BadCredentialsException e) {
      throw new InvalidCredentialsException();
    }
  }
}



