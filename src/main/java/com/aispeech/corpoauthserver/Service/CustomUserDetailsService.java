package com.aispeech.corpoauthserver.Service;

import com.aispeech.corpoauthserver.Model.CustomUserDetails;
import com.aispeech.corpoauthserver.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/25 0025 15:20
 */

@Component("myUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    LdapAuthenticationService ldapAuthenticationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();
        if (authInfo != null && authInfo.isAuthenticated()) {
            User user = ldapAuthenticationService.getDnFromUser(username);
            logger.info("user has already authenticated: {}", username);
            return new CustomUserDetails(username, "authenticate already",
                    AuthorityUtils.commaSeparatedStringToAuthorityList("USER"),
                    user.getPersonName(), user.getEmail(), user.getDn());
        }
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String password = request.getParameter("password");
        User user = ldapAuthenticationService.ldapAuth(username, password);
        if (null == user) {
            logger.info("authenticated failed, username or password wrong: {}", username);
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        return new CustomUserDetails(username, password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("USER"),
                user.getPersonName(), user.getEmail(), user.getDn());
    }

}
