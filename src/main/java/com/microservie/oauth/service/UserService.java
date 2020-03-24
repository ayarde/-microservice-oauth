package com.microservie.oauth.service;

import com.microservie.oauth.client.UserFeignClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUser, UserDetailsService {
    private Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserFeignClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            com.microservice.common.user.modal.entity.User user = client.findByUsername(username);

            List<GrantedAuthority> authorities = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .peek(authority -> log.info("Role:" + authority.getAuthority()))
                    .collect(Collectors.toList());

            log.info("Authenticated user" + username);

            return new User(user.getUsername(), user.getPassword(), user.getEnable(), true,
                    true, true, authorities);
        } catch (FeignException feignException){
            log.error("Error during login, user '" + username + "' not exist in system");
            throw new UsernameNotFoundException("Error during login, user '" + username + "' not exist in system");
        }
    }

    @Override
    public com.microservice.common.user.modal.entity.User findByUsername(String userName) {
        return client.findByUsername(userName);
    }

    @Override
    public com.microservice.common.user.modal.entity.User update(com.microservice.common.user.modal.entity.User user, Long id) {
        return client.update(user,id);
    }

}
