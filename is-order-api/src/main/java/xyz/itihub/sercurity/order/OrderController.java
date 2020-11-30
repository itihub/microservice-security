package xyz.itihub.sercurity.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    public OrderInfo create(@RequestBody OrderInfo info, @RequestHeader String username){
        log.info("userName is {}", username);
//        log.info("userId is {}", user.getId());
//        PriceInfo priceInfo = restTemplate.getForObject("http://localhost:9080/prices/" + info.getProductId(), PriceInfo.class);
//        log.info("price is {}", priceInfo.getPrice());
        return info;
    }

    @GetMapping("/{id}")
    public OrderInfo create(@PathVariable Long id, @RequestHeader String username){
        log.info("orderId is {}", id);
        log.info("userId is {}", username);
        return new OrderInfo();
    }

}
