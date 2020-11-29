package xyz.itihub.sercurity.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import xyz.itihub.sercurity.server.resource.User;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 获取用户名 @AuthenticationPrincipal String username
     * 获取用户对象 @AuthenticationPrincipal User user
     * 获取用户对象中指定属性 @AuthenticationPrincipal(expression = "#this.id") User user
     */
    @PostMapping
    public OrderInfo create(@RequestBody OrderInfo info, @AuthenticationPrincipal User user){
        log.info("userName is {}", user.getUsername());
        log.info("userId is {}", user.getId());
//        PriceInfo priceInfo = restTemplate.getForObject("http://localhost:9080/prices/" + info.getProductId(), PriceInfo.class);
//        log.info("price is {}", priceInfo.getPrice());
        return info;
    }

    @GetMapping("/{id}")
    public OrderInfo create(@PathVariable Long id, @AuthenticationPrincipal(expression = "#this.id") Long userId){
        log.info("orderId is {}", id);
        log.info("userId is {}", userId);
        return new OrderInfo();
    }

}
