package com.microservie.oauth.event;

import com.microservice.common.user.modal.entity.User;
import com.microservie.oauth.service.IUser;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    private Logger logger = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    @Autowired
    private IUser iUser;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        UserDetails userDetails =  (UserDetails) authentication.getPrincipal();
        String message = "Success Login: " + userDetails.getUsername();

        System.out.println(message);
        logger.info(message);

        User user = iUser.findByUsername(authentication.getName());

        if(user.getTries() != 0 && user.getTries() > 0){
            user.setTries(0);
            iUser.update(user,user.getId());
        }
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException e, Authentication authentication) {
        String message = "Error in login: " + e.getMessage();

        System.out.println(message);
        logger.error(message);

        try {
            User user = iUser.findByUsername(authentication.getName());
            if(user.getTries() == null){
                user.setTries(0);
            }

            logger.info("Current Attempts is: " + user.getTries());

            user.setTries(user.getTries()+1);

            logger.info("Current Attempts after: " + user.getTries());

            if (user.getTries() >= 3){
                logger.error(String.format("The user %s was disable by maximum tries", user.getUsername()));
                user.setEnable(false);
            }

            iUser.update(user, user.getId());

        } catch (FeignException feignException){
            logger.error(String.format("The user %s not exist in the system", authentication.getName()));
        }

    }
}
