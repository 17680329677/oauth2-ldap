package com.aispeech.corpoauthserver.Config;

import com.aispeech.corpoauthserver.Handler.CustomAuthenticationFailHandler;
import com.aispeech.corpoauthserver.Handler.CustomAuthenticationSuccessHandler;
import com.aispeech.corpoauthserver.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/25 0025 13:16
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableGlobalAuthentication
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String OAUTH_PREFIX = "URL_PREFIX";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private CustomAuthenticationFailHandler customAuthenticationFailHandler;
    @Autowired
    private Environment environment;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置使用自定义的UserDetailsService进行认证
        auth.userDetailsService(customUserDetailsService);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String prefix = environment.getProperty(OAUTH_PREFIX, "");
        http.csrf().disable();
        http
                .requestMatchers()
                .antMatchers(prefix + "/oauth/**","/login/**","/logout/**", prefix + "/user/login", "/oauth/login")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin()
                .loginPage(prefix + "/auth/login")
                .loginProcessingUrl(prefix + "/user/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailHandler)
                .and()
                .logout()
                .logoutUrl(prefix + "/oauth/logout")
                .deleteCookies("JSESSIONID");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/oauth/token");
    }

    /**
     * Spring Boot 2 配置，这里要bean 注入,否则@Autowired失败
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // 注入此Bean方便认证时获取用户输入的信息
    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
}
