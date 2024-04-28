package com.andrade.SpringJWTAuthenticator.model.exceptions;

public class AccountAlreadyVerifiedOrWrongLinkException extends RuntimeException {
  public AccountAlreadyVerifiedOrWrongLinkException() {
    super("Account already verified or wrong link");
  }
}
