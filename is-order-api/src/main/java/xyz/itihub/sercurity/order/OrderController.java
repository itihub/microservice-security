package xyz.itihub.sercurity.order;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    /**
     * 使用OAuth封装的RestTemplate进行微服务之间通讯会进行token传递
     */
    private final OAuth2RestTemplate restTemplate;

    /**
     * 获取用户名 @AuthenticationPrincipal String username
     * 获取用户对象 @AuthenticationPrincipal User user
     * 获取用户对象中指定属性 @AuthenticationPrincipal(expression = "#this.id") User user
     */
    @PostMapping
//    @PreAuthorize("#oauth2.hasAnyScope('read')") // 控制应用的访问权限
//    @PreAuthorize("hasRole('ROLE_ADMIN')") // 控制用户的访问权限
    @SentinelResource(value = "createOrder", blockHandler = "doOnBlock")  // sentinel 注解方式
    public OrderInfo create(@RequestBody OrderInfo info, @AuthenticationPrincipal String username) throws InterruptedException {

        log.info("userName is {}", username);

        Thread.sleep(50L);

        // sentinel 定义资源 代码方式配置
//        try (Entry entry = SphU.entry("createOrder")) {
//            log.info("userName is {}", username);
//        }catch (BlockException e){
//            log.info("block");
//        }

//        log.info("userId is {}", user.getId());
//        PriceInfo priceInfo = restTemplate.getForObject("http://localhost:9080/prices/" + info.getProductId(), PriceInfo.class);
//        log.info("price is {}", priceInfo.getPrice());
        return info;
    }

    // 降级函数
    public OrderInfo doOnBlock(@RequestBody OrderInfo info, @AuthenticationPrincipal String username, BlockException exception){
        log.info("blocked by" + exception.getClass().getSimpleName());
        return info;
    }

    @GetMapping("/{id}")
    @SentinelResource(value = "getOrder")
    public OrderInfo getInfo(@PathVariable Long id, @AuthenticationPrincipal String username){
        log.info("orderId is {}", id);
        log.info("userName is {}", username);
        OrderInfo info = new OrderInfo();
        info.setId(id);
        info.setProductId(id * 5);
        return info;
    }

}
