package com.aispeech.corpoauthserver.Config;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/25 0025 13:33
 */

import com.aispeech.corpoauthserver.Service.CustomUserDetailsService;
import com.aispeech.corpoauthserver.Utils.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * OAuth 授权服务器配置
 * @author hezhe.du
 * @date 2019-07-10
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final String SIGNING_KEY = "JWT_SIGNING_KEY";

    private static final String OAUTH_PREFIX = "URL_PREFIX";

    @Autowired
    private Environment environment;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Resource
    private DataSource dataSource;

    // token转jwt convert
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(this.environment.getProperty(SIGNING_KEY, "aispeech"));
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    // 从数据库读取clients信息
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        String prefix = environment.getProperty(OAUTH_PREFIX, "");
        // 将增强的token设置到增强链中
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer(), accessTokenConverter()));
        endpoints
                .tokenStore(tokenStore())
                .authorizationCodeServices(authorizationCodeServices())
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(enhancerChain)
                .userDetailsService(userDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .pathMapping("/oauth/authorize", prefix + "/oauth/authorize")
                .pathMapping("/oauth/token", prefix + "/oauth/token")
                .pathMapping("/oauth/check_token", prefix + "/oauth/check_token")
                .pathMapping("/oauth/confirm_access",prefix + "/oauth/confirm_access"); // 配置自定义授权页面

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        //允许表单认证
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");

        oauthServer.allowFormAuthenticationForClients();
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    // 配置token增强，token存入额外信息
    @Bean
    public TokenEnhancer customTokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    // 配置授权码code自定义授权规则
    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new CustomJdbcAuthorizationCodeServices(dataSource);
    }
}
