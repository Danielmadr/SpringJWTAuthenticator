package com.andrade.SpringJWTAuthenticator.model;


import com.andrade.SpringJWTAuthenticator.dto.UserRequestDTO;
import com.andrade.SpringJWTAuthenticator.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  private String login = this.email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserStatus status;

  public User(UserRequestDTO userDTO) {
    BeanUtils.copyProperties(userDTO, this);
    this.login = this.email;
    this.status = UserStatus.PENDING;
  }
}
