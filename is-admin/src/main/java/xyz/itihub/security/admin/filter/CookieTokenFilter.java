package xyz.itihub.security.admin.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import xyz.itihub.security.admin.controller.TokenInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieTokenFilter extends ZuulFilter {

  private RestTemplate restTemplate = new RestTemplate();

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletResponse response = requestContext.getResponse();

    String accessToken = getCookie("itihub_access_token");
    if (StringUtils.isNotBlank(accessToken)){
      requestContext.addZuulRequestHeader("Authorization", "bearer " + accessToken);

    }else {

      String refreshToken = getCookie("itihub_refresh_token");
      if (StringUtils.isNotBlank(refreshToken)){
        String oauthServiceUrl = "http://gateway.itihub.com:9070/token/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("admin", "123456");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params);

        try {
          ResponseEntity<TokenInfo> newToken = restTemplate.exchange(oauthServiceUrl,  HttpMethod.POST, entity, TokenInfo.class);

          requestContext.addZuulRequestHeader("Authorization", "bearer " + newToken.getBody().getAccess_token());

          Cookie accessTokenCookie = new Cookie("itihub_access_token", newToken.getBody().getAccess_token());
          accessTokenCookie.setMaxAge(newToken.getBody().getExpires_in().intValue() - 3);
          accessTokenCookie.setDomain("itihub.com");
          accessTokenCookie.setPath("/");
          response.addCookie(accessTokenCookie);

          Cookie refreshTokenCookie = new Cookie("itihub_refresh_token", newToken.getBody().getRefresh_token());
          refreshTokenCookie.setMaxAge(2592000);
          refreshTokenCookie.setDomain("itihub.com");
          refreshTokenCookie.setPath("/");
          response.addCookie(refreshTokenCookie);

        }catch (Exception e){
          // refresh_token 过期 失败响应
          requestContext.setSendZuulResponse(false);
          requestContext.setResponseStatusCode(500);
          requestContext.setResponseBody("{\"messages\":\"refresh fail\"}");
          requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
      }else {
        // refresh_token 过期 失败响应
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(500);
        requestContext.setResponseBody("{\"messages\":\"refresh fail\"}");
        requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      }
    }

    return null;
  }

  private String getCookie(String name){
    String result = null;

    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();

    Cookie[] cookies = request.getCookies();

    for (Cookie cookie : cookies) {
      if (StringUtils.equalsIgnoreCase(name, cookie.getName())){
        return cookie.getValue();
      }
    }

    return result;
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

}
