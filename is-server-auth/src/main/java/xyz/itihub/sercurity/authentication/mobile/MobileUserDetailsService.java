package xyz.itihub.sercurity.authentication.mobile;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MobileUserDetailsService {

    MobileUserDetails loadUserByMobile(String var1) throws UsernameNotFoundException;
}
