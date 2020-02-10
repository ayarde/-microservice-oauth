package com.microservie.oauth.security;

import com.microservice.common.user.modal.entity.User;
import com.microservie.oauth.service.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InfoAdditionalToken implements TokenEnhancer {
    @Autowired
    private IUser iUser;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<String, Object>();
        User user = iUser.findByUsername(oAuth2Authentication.getName());
        info.put("name", user.getName());
        info.put("lastName", user.getLastname());
        info.put("email", user.getEmail());
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}
