package xyz.itihub.security.admin.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenInfo {

  private String access_token;
  private String token_type ;
  private String expires_in ;
  private String scope ;
}
