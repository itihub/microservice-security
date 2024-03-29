package xyz.itihub.sercurity.price;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "prices")
public class PriceController {

    @GetMapping("/{id}")
    public PriceInfo getPrice(@PathVariable Long id, @AuthenticationPrincipal String username){
        log.info("productId is {}", id);
        log.info("user is {}", username);
        PriceInfo info = new PriceInfo();
        info.setId(id);
        info.setPrice(new BigDecimal(100));
        return info;
    }
}
