package com.andrade.SpringJWTAuthenticator.service;


import com.andrade.SpringJWTAuthenticator.model.User;
import com.andrade.SpringJWTAuthenticator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDatailServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findByLogin(username).orElseThrow(
            () -> new UsernameNotFoundException("User not found"));
    return UserDetailsImpl.build(user);
  }
}
