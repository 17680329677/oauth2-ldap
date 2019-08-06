package com.aispeech.corpoauthserver.Config;//package com.aispeech.dashboard.Config;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @Author: hezhe.du
 * @Date: 2019/7/11 0011 10:49
 */

@Configuration
public class LdapConfiguration {

    private static final String LDAP_URL = "LDAP_URL";
    private static final String LDAP_BASE = "LDAP_BASE";
    private static final String LDAP_USERNAME = "LDAP_USERNAME";
    private static final String LDAP_PASSWORD = "LDAP_PASSWORD";

    @Autowired
    private Environment environment;

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(environment.getProperty(LDAP_URL, ""));
        contextSource.setBase(environment.getProperty(LDAP_BASE, ""));
        System.out.println(environment.getProperty(LDAP_BASE));
        contextSource.setUserDn(environment.getProperty(LDAP_USERNAME, ""));
        contextSource.setPassword(environment.getProperty(LDAP_PASSWORD, ""));
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }
}
