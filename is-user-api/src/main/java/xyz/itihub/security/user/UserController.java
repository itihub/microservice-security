package xyz.itihub.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.itihub.security.util.ThreadLocalUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;

    private final UserService userService;

    @GetMapping("login")
    public void login(@Validated UserInfo user, HttpServletRequest request){
        UserInfo info = userService.login(user);
        if (info == null){
            throw new RuntimeException("用户名或密码错误");
        }
        // 防止Session Fixation 攻击
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }
        request.getSession(true).setAttribute("user", info);
    }

    @GetMapping("logout")
    public void logout(HttpServletRequest request){
        request.getSession().invalidate();
    }

    @PostMapping
    public UserInfo create(@RequestBody UserInfo user){
        return userService.create(user);
    }

    @PutMapping(value = "{id}")
    public UserInfo update(UserInfo user){
        return userService.update(user);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }

    @GetMapping(value = "{id}")
    public UserInfo get(@PathVariable Long id, HttpServletRequest request){
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        if (user == null || !user.getId().equals(id)){
            throw new RuntimeException("身份认证信息异常，获取用户信息失败");
        }
        return userService.get(id);
    }

    @GetMapping
    public List<UserInfo> query(String name){
        return userService.query(name);
    }

    /**
     * jdbcTemplate 方式
     * sql 注入攻击
     * localhost:8080/users?name='or 1=1 or name='
     * @param name
     * @return
     */
//    @GetMapping
//    public List query(String name){
//        String sql = "SELECT id, name FROM user WHERE name = '" + name + "'";
//        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
//        return data;
//    }

    /**
     * JPA 方式
     * @param name
     * @return
     */
//    @GetMapping
//    public List<User> query(String name){
//        List<User> data = userRepository.findByName(name);
//        return data;
//    }
}
