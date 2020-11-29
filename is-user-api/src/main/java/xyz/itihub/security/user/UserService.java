package xyz.itihub.security.user;

import java.util.List;

public interface UserService {

    UserInfo create(UserInfo userInfo);

    UserInfo update(UserInfo userInfo);

    void delete(Long id);

    UserInfo get(Long id);

    List<UserInfo> query(String name);

    UserInfo login(UserInfo user);
}
