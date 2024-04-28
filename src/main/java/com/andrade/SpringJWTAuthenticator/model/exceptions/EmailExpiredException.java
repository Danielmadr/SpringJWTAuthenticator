package com.andrade.SpringJWTAuthenticator.model.exceptions;

public class EmailExpiredException extends RuntimeException {

  public EmailExpiredException() {
    super("Link de verificação expirado. Um novo link foi enviado para o seu e-mail.");
  }
}
