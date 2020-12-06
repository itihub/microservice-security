package xyz.itihub.security.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OAuthController {

  private RestTemplate restTemplate = new RestTemplate();

  @GetMapping(value = "oauth/callback")
  public void callback(@RequestParam(value = "code") String code,
                    String state,
                    HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.info("state is {}", state);

    String oauthServiceUrl = "http://localhost:9070/token/oauth/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBasicAuth("admin", "123456");

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("grant_type", "authorization_code");
    params.add("redirect_uri", "http://admin.itihub.com:8080/oauth/callback");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

    ResponseEntity<TokenInfo> token = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
    request.getSession().setAttribute("token", token.getBody().init());

    response.sendRedirect("/");
  }

  @GetMapping(value = "me")
  public TokenInfo me(HttpServletRequest request){
    TokenInfo token = (TokenInfo) request.getSession().getAttribute("token");
    return token;
  }

  @GetMapping(value = "logout")
  public void logout(HttpServletRequest request){
    request.getSession().invalidate();
  }

}
