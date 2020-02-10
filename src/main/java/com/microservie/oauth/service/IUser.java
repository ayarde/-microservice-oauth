package com.microservie.oauth.service;

import com.microservice.common.user.modal.entity.User;

public interface IUser {
    public User findByUsername(String userName);
}
