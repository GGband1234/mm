package org.example.client;

import org.example.config.DefaultFeignConfig;
import org.example.domain.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mm-user", configuration = DefaultFeignConfig.class)
public interface UserClient {

    @GetMapping("/user/findByName/{username}")
    User findByUserName(@PathVariable("username") String username);

    @GetMapping("/user/list")
    List<User> listByUserName(@RequestParam("username") String username);

    @GetMapping("/user/findById/{userId}")
    User findByUserId(@PathVariable("userId") Integer userId);
}
