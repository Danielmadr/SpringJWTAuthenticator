package com.andrade.SpringJWTAuthenticator.service;

import java.util.ArrayList;
import java.util.Collection;

import com.andrade.SpringJWTAuthenticator.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



public class UserDetailsImpl implements UserDetails {

  private final String username;
  private final String password;
  public UserDetailsImpl(String username,
                         String password,
                         Collection<? extends GrantedAuthority> authorities) {
    super();
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(User user) {

    return new UserDetailsImpl(
            user.getLogin(),
            user.getPassword(),
            new ArrayList<>());
  }

  private final Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true; //A princípio o usuário nunca expira
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;//A princípio o usuário nunca fica bloqueado
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; //A princípio o usuário nunca expira
  }

  @Override
  public boolean isEnabled() {
    return true; //A princípio o usuário nunca expira
  }
}
