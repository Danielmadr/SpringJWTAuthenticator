package com.andrade.SpringJWTAuthenticator.model.exceptions;

public class EmailSendingException extends RuntimeException {
  public EmailSendingException() {
    super("Erro ao enviar email");
  }
}
