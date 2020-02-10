package com.microservie.oauth.client;

import com.microservice.common.user.modal.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="user-app")
public interface UserFeignClient {
    @GetMapping("/user/search/findUser")
    public User findByUsername(@RequestParam String username);
}
