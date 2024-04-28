package com.andrade.SpringJWTAuthenticator.service;


import com.andrade.SpringJWTAuthenticator.dto.AuthenticationDTO;
import com.andrade.SpringJWTAuthenticator.dto.UserRequestDTO;
import com.andrade.SpringJWTAuthenticator.dto.VerifyMessage;
import com.andrade.SpringJWTAuthenticator.model.User;
import com.andrade.SpringJWTAuthenticator.model.UserVerifier;
import com.andrade.SpringJWTAuthenticator.model.enums.UserStatus;
import com.andrade.SpringJWTAuthenticator.model.exceptions.*;
import com.andrade.SpringJWTAuthenticator.repository.UserRepository;
import com.andrade.SpringJWTAuthenticator.repository.UserVerifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  private final UserVerifierRepository verifierRepository;

  private final PasswordEncoder passwordEncoder;

  private final EmailService emailService;

  private final Instant VERIFY_EMAIL_EXPIRATION = now().plusMillis(900000);

  @Value("${project.dominio}")
  private String dominio;

  public void saveNewUser(UserRequestDTO user) {
    if (userRepository.existsByLogin(user.email())) {
      throw new UserAlreadyRegisteredException(user.email());
    }

    User newUser = createUserFromDTO(user);
    UserVerifier verifier = createVerifier(newUser);
    sendWelcomeEmail(verifier);
  }

  private User createUserFromDTO(UserRequestDTO user) {
    if (!isValidPassword(user.password())) {
      throw new InvalidPasswordException();
    }

    User newUser = new User(user);
    newUser.setPassword(encryptPassword(user.password()));
    userRepository.save(newUser);
    return newUser;
  }

  private boolean isValidPassword(String password) {
    return password != null && password.length() >= 8;
  }

  private UserVerifier createVerifier(User user) {
    UserVerifier verifier = new UserVerifier();
    verifier.setUser(user);
    verifier.setIdentifier(UUID.randomUUID());
    verifier.setExpirationDate(VERIFY_EMAIL_EXPIRATION);
    verifierRepository.save(verifier);
    return verifier;
  }

  private String encryptPassword(String password) {
    return passwordEncoder.encode(password);
  }

  private void sendWelcomeEmail(UserVerifier user) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_HTML);

    String subject = "Bem-vindo(a) ao SkyFeedConnect";
    String activationLink = dominio + "users/activate/" + user.getIdentifier();
    String emailContent = String.format("""
            <html>
            <body style="font-family: Arial, sans-serif;">
            
            <h2>Olá!</h2>
            
            <br/>
            
            <p>Obrigado por se cadastrar. Para ativar sua conta, clique no link abaixo:</p>
            
            <a href="%s" style="padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; margin-botton:10px;">Ativar Conta</a>
            
            <br/>
            <br/>
            
            <p>Se o botão acima não funcionar, copie e cole o seguinte URL em seu navegador:</p>
            <p>%s</p>
            
            <br/>
            
            <p>Obrigado,</p>
            <p>Equipe SkyFeed</p>
            
            </body>
            </html>
            """, activationLink, activationLink);
    try {
      emailService.sendEmail(user.getUser().getEmail(), subject, emailContent, headers);
      logger.info("Email enviado para: {}", user.getUser().getEmail());
    } catch (Exception e) {
      logger.error("Falha ao enviar o email de verificação", e);
      throw new EmailSendingException();
    }
  }

  public VerifyMessage verifyUser(String uuid) {

    UserVerifier user = verifierRepository.findByIdentifier(UUID.fromString(uuid))
            .orElseThrow(AccountAlreadyVerifiedOrWrongLinkException::new);

    if (user.getExpirationDate().isBefore(now())) {
      user.setIdentifier(UUID.randomUUID());
      user.setExpirationDate(VERIFY_EMAIL_EXPIRATION);
      resendVerificationEmail(user);
      return new VerifyMessage("Link expirado. Enviamos um novo link de verificação para seu email.");
    }

    User userToVerify = user.getUser();
    userToVerify.setStatus(UserStatus.ACTIVE);

    verifierRepository.delete(user);

    return new VerifyMessage("Email verificado com sucesso!");
  }

  private void resendVerificationEmail(UserVerifier user) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_HTML);

    String subject = "Link de Verificação Atualizado";
    String activationLink = dominio + "users/activate/" + user.getIdentifier();
    String emailContent = String.format("""
            <html>
            <body style="font-family: Arial, sans-serif;">

            <h2>Olá!</h2>
            
            <p>Reenviamos este e-mail porque o link de ativação anterior expirou.</p>
            <p>Clique no link abaixo para ativar sua conta:</p>
            
            <a href="%s" style="padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px;">Ativar Conta</a>
            
            <p>Se o botão acima não funcionar, copie e cole o seguinte URL em seu navegador:</p>
            <p>%s</p>
            
            <p>Este link expirará em 15 minutos.</p>
            
            <p>Obrigado,</p>
            <p>Sua Equipe</p>
            
            </body>
            </html>
            """, activationLink, activationLink);
    try {
      emailService.sendEmail(user.getUser().getEmail(), subject, emailContent, headers);
      logger.info("Email enviado para: {}", user.getUser().getEmail());
    } catch (Exception e) {
      logger.error("Falha ao enviar o email de verificação", e);
      throw new EmailSendingException();
    }
  }
}
