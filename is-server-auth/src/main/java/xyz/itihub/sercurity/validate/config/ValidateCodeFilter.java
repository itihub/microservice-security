package xyz.itihub.sercurity.validate.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.itihub.sercurity.AuthConstant;
import xyz.itihub.sercurity.props.SecurityProperties;
import xyz.itihub.sercurity.props.ValidateCodeProperties;
import xyz.itihub.sercurity.validate.ValidateCodeProcessorHolder;
import xyz.itihub.sercurity.validate.ValidateCodeType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    private final SecurityProperties securityProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private final ValidateCodeProcessorHolder validateCodeProcessorHolder;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        ValidateCodeProperties validateCodeProperties = securityProperties.getCode();

        urlMap.put(AuthConstant.DEFAULT_LOGIN_PROCESSING_URL_OAUTH, ValidateCodeType.IMAGE);
        addUrlToMap(validateCodeProperties.getImage().getUrl(), ValidateCodeType.IMAGE);

        urlMap.put(AuthConstant.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrlToMap(validateCodeProperties.getSms().getUrl(), ValidateCodeType.SMS);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        ValidateCodeType validateCodeType = getValidateCodeType(httpServletRequest);
        if (validateCodeType != null){
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(validateCodeType)
                        .validate(new ServletWebRequest(httpServletRequest, httpServletResponse));
            } catch (AuthenticationException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    protected void addUrlToMap(String urlString, ValidateCodeType type) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, type);
            }
        }
    }

    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        Set<String> urls = urlMap.keySet();
        for (String url : urls) {
            //查询是否有匹配的请求路径
            String[] httpUri = StringUtils.splitByWholeSeparatorPreserveAllTokens(url, " ");
            boolean methodFlag = false;
            String uri;
            if (httpUri.length == 2){
                methodFlag = StringUtils.equalsIgnoreCase(request.getMethod(), httpUri[0]);
                uri = httpUri[1];
            }else {
                methodFlag = true;
                uri = httpUri[0];
            }
            if (pathMatcher.match(uri, request.getRequestURI()) && methodFlag) {
                //获取验证类型
                result = urlMap.get(url);
            }
        }
        return result;
    }
}
