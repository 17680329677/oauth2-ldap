package com.aispeech.corpoauthserver.Config;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/25 0025 13:26
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置 暂时资源只有用户信息
 * @Author: hezhe.du
 * @Date: 2019/7/2 0002 18:06
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "speech";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.requestMatchers().antMatchers("/user/info")
                .and()
                .authorizeRequests()
                .antMatchers("/user/info").authenticated();
    }
}