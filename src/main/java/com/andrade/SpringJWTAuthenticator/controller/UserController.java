package com.andrade.SpringJWTAuthenticator.controller;


import com.andrade.SpringJWTAuthenticator.dto.AuthenticationDTO;
import com.andrade.SpringJWTAuthenticator.dto.UserRequestDTO;
import com.andrade.SpringJWTAuthenticator.dto.VerifyMessage;
import com.andrade.SpringJWTAuthenticator.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/save")
  public ResponseEntity<?> saveNewUser(@RequestBody UserRequestDTO user, UriComponentsBuilder uriComponentsBuilder) {
    this.userService.saveNewUser(user);
    var uri = uriComponentsBuilder.path("/users/all").build().toUri();
    return ResponseEntity.created(uri).build();
  }

  @GetMapping("/activate/{uuid}")
  public ResponseEntity<VerifyMessage> verifyUser(@PathVariable String uuid) {
    return ResponseEntity.ok(this.userService.verifyUser(uuid));
  }
}
