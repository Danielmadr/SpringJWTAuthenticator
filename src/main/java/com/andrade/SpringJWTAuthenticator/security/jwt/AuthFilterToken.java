package com.andrade.SpringJWTAuthenticator.security.jwt;

import java.io.IOException;

import com.andrade.SpringJWTAuthenticator.service.UserDatailServiceImpl;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilterToken extends OncePerRequestFilter {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDatailServiceImpl userDatailService;

  @Override
  protected void doFilterInternal(
          @Nonnull HttpServletRequest request,
          @Nonnull HttpServletResponse response,
          @Nonnull FilterChain filterChain) throws ServletException, IOException {

    String jwt = getToken(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      try {
        String username = jwtUtils.extractUsernameFromJwtToken(jwt);

        UserDetails userDetails = userDatailService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(auth);

      } catch (Exception e) {
        logger.error("Erro ao processar token JWT e configurar autenticação.", e);
        throw new ServletException("Falha ao processar token JWT e configurar autenticação.", e);
      }
    }
    filterChain.doFilter(request, response);
  }

  private String getToken(HttpServletRequest request) {
    String headerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(headerToken) && headerToken.startsWith("Bearer")) {
      return headerToken.replace("Bearer ", "");
    }
    return null;
  }
}
