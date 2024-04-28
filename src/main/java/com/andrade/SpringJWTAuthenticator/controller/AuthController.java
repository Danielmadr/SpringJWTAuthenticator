package com.andrade.SpringJWTAuthenticator.controller;


import com.andrade.SpringJWTAuthenticator.dto.AccessDTO;
import com.andrade.SpringJWTAuthenticator.dto.AuthenticationDTO;
import com.andrade.SpringJWTAuthenticator.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping(value = "/login")
  public ResponseEntity<AccessDTO> login(@RequestBody AuthenticationDTO loginInfo) {
    AccessDTO access = this.authService.login(loginInfo);
    System.out.println(access);
    return ResponseEntity.ok(access);
  }
}
