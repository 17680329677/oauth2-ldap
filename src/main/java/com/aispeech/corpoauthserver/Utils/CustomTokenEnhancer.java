package com.aispeech.corpoauthserver.Utils;

import com.aispeech.corpoauthserver.Model.CustomUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置tokenEnhancer，在jwt中存储额外的信息
 * @Author: hezhe.du
 * @Date: 2019/7/11 0011 15:28
 */

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("personName", user.getPersonName());
        additionalInfo.put("mail", user.getMail());
        additionalInfo.put("userDn", user.getUserDn());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
