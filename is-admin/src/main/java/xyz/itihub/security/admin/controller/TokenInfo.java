package xyz.itihub.security.admin.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TokenInfo {

  private String access_token;
  private String refresh_token;
  private String token_type ;
  private Long expires_in ;
  private String scope ;


  private LocalDateTime expireTime;

  public TokenInfo init() {
    expireTime = LocalDateTime.now().plusSeconds(expires_in - 3);
    return this;
  }

  @JsonIgnore
  public boolean isExpired() {
    return expireTime.isBefore(LocalDateTime.now());
  }

}
