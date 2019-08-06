package com.aispeech.corpoauthserver.Config;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;

import javax.sql.DataSource;

/**
 * 自定义授权码的生成规则,授权码存在数据库中
 * 换取token后自动删除
 * @Author: hezhe.du
 * @Date: 2019/7/11 0011 18:14
 */

public class CustomJdbcAuthorizationCodeServices extends JdbcAuthorizationCodeServices {
    private RandomValueStringGenerator generator;
    public CustomJdbcAuthorizationCodeServices(DataSource dataSource) {
        super(dataSource);
        this.generator = new RandomValueStringGenerator(32);
    }
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = this.generator.generate();
        store(code, authentication);
        return code;
    }
}
