package xyz.itihub.security.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权过滤器
 */
@Slf4j
@Component
public class AuthorizationFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("authorization start");

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        if (isNeedAuth(request)){

            TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");

            if (tokenInfo != null && tokenInfo.isActive()){
                if (!hasPermission(tokenInfo, request.getRequestURI())) {
                    log.info("audit log update fail 403");
                    handleError(403, requestContext);
                }

                // 存放用户信息
                requestContext.addZuulRequestHeader("username", tokenInfo.getUser_name());

            }else {
                if (!StringUtils.startsWith(request.getRequestURI(), "/token")){
                    log.info("audit log update fail 401");
                    handleError(401, requestContext);
                }
            }
        }

        return null;
    }

    private boolean hasPermission(TokenInfo tokenInfo, String requestURI) {
        // TODO: 与数据库交互 此处模拟
        return RandomUtils.nextInt() % 2 == 0;
    }

    private void handleError(int status, RequestContext requestContext) {
        requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        requestContext.setResponseStatusCode(status);
        requestContext.setResponseBody("{\"message\":\"auth fail\"}");
        requestContext.setSendZuulResponse(false); // 直接返回，停止往下走
    }

    private boolean isNeedAuth(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // TODO: 与数据库交互判断是否需要请求权限
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }
}
