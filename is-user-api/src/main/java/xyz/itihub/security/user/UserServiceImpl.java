package xyz.itihub.security.user;

import com.lambdaworks.crypto.SCryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserInfo create(UserInfo userInfo) {
        User user = new User();
        BeanUtils.copyProperties(userInfo, user);
        user.setPassword(SCryptUtil.scrypt(user.getPassword(), 32768, 8, 1));
        userRepository.save(user);
        userInfo.setId(user.getId());
        return userInfo;
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        return null;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserInfo get(Long id) {
        return userRepository.findById(id).get().buildInfo();
    }

    @Override
    public List<UserInfo> query(String name) {
        return userRepository.findByNameLike(name).stream().map(User::buildInfo).collect(Collectors.toList());
    }

    @Override
    public UserInfo login(UserInfo info) {
        User user = userRepository.findByUsername(info.getUsername());
        if (user != null && SCryptUtil.check(info.getPassword(), user.getPassword())){
            return user.buildInfo();
        }
        return null;
    }
}
