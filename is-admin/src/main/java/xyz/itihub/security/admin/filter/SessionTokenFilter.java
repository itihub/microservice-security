package xyz.itihub.security.admin.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import xyz.itihub.security.admin.controller.TokenInfo;

import javax.servlet.http.HttpServletRequest;

//@Component
public class SessionTokenFilter extends ZuulFilter {

  private RestTemplate restTemplate = new RestTemplate();

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();

    TokenInfo token = (TokenInfo) request.getSession().getAttribute("token");
    if (token != null){
      String tokenValue = token.getAccess_token();

      // 判断refresh_token是否过期，过期则刷新token
      if (token.isExpired()){
        String oauthServiceUrl = "http://gateway.itihub.com:9070/token/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("admin", "123456");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", token.getRefresh_token());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params);

        try {
          ResponseEntity<TokenInfo> newToken = restTemplate.exchange(oauthServiceUrl,  HttpMethod.POST, entity, TokenInfo.class);
          request.getSession().setAttribute("token", newToken.getBody().init());
          tokenValue = newToken.getBody().getAccess_token();
        }catch (Exception e){
          // refresh_token 过期 失败响应
          requestContext.setSendZuulResponse(false);
          requestContext.setResponseStatusCode(500);
          requestContext.setResponseBody("{\"messages\":\"refresh fail\"}");
          requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }

      }
      requestContext.addZuulRequestHeader("Authorization", "bearer " + tokenValue);
    }
    return null;
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

}
