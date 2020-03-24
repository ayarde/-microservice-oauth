package com.microservie.oauth.service;

import com.microservice.common.user.modal.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUser {
    public User findByUsername(String userName);
    public User update(User user, Long id);
}
