package xyz.itihub.sercurity.server.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import xyz.itihub.sercurity.authentication.mobile.MobileUser;
import xyz.itihub.sercurity.authentication.mobile.MobileUserDetails;
import xyz.itihub.sercurity.authentication.mobile.MobileUserDetailsService;

@RequiredArgsConstructor
@Component
public class UserDetailServiceImpl implements UserDetailsService, MobileUserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: 模拟用户 可替换读取数据库
        return User.withUsername(username)
                .password(passwordEncoder.encode("123456"))
                // hasRole 设置角色
                .authorities("ROLE_ADMIN")
                .build();
    }

    @Override
    public MobileUserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        MobileUserDetails mobileUserDetails = new MobileUser("admin",
                passwordEncoder.encode("123456"), true, true,
                true, true, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        return mobileUserDetails;
    }
}
