package com.andrade.SpringJWTAuthenticator.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  @Value("${spring.mail.username}")
  private String sender;

  private final JavaMailSender javaMailSender;

  public void sendEmail(String destiny, String subject, String body, HttpHeaders headers) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();

    // Use MimeMessageHelper para configurar o e-mail
    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8"); // true indica que o e-mail terá conteúdo HTML

    // Defina o remetente, destinatário, assunto e corpo do e-mail
    helper.setFrom(sender);
    helper.setTo(destiny);
    helper.setSubject(subject);
    helper.setText(body, true); // O segundo parâmetro true indica que o corpo é HTML

    // Envie o e-mail
    javaMailSender.send(message);
    System.err.println("Email de verificação enviado"); // Adicionando um log
  }
}
