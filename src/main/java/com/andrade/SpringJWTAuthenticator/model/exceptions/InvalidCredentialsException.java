package com.andrade.SpringJWTAuthenticator.model.exceptions;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid login or password");
  }
}
