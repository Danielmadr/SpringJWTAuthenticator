package com.andrade.SpringJWTAuthenticator.model.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String login) {
    super("Usuário '" + login + "' não encontrado!");
  }
}
