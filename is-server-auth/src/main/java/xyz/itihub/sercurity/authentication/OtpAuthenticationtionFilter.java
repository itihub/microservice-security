package xyz.itihub.sercurity.authentication;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import xyz.itihub.sercurity.AuthConstant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class OtpAuthenticationtionFilter extends AbstractAuthenticationProcessingFilter {

    private String mobileParameter = AuthConstant.DEFAULT_PARAMETER_NAME_MOBILE;

    private boolean postOnly = true;

    public OtpAuthenticationtionFilter() {
        super(new AntPathRequestMatcher(AuthConstant.DEFAULT_LOGIN_PROCESSING_URL_MOBILE));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if (postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String mobile = this.obtainMobile(request);
        mobile = Optional.ofNullable(mobile).orElse("");

        mobile = mobile.trim();

        OtpAuthenticationToken authRequest = new OtpAuthenticationToken(mobile);

        this.setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              OtpAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
