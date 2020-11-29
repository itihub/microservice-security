package xyz.itihub.security.config;

import org.springframework.util.Base64Utils;

public class Test {

    public static void main(String[] args) {
        String base64 = Base64Utils.encodeToString("jizhe:123456".getBytes());
        System.out.println(base64);
    }
}
