package com.aispeech.corpoauthserver.Service.impl;

import com.aispeech.corpoauthserver.Controller.AuthController;
import com.aispeech.corpoauthserver.Model.User;
import com.aispeech.corpoauthserver.Service.LdapAuthenticationService;
import com.aispeech.corpoauthserver.Utils.UserAttributesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import java.util.List;

/**
 * @Author: hezhe.du
 * @Date: 2019/7/11 0011 11:07
 */

@Service
public class LdapAuthenticationServiceImpl implements LdapAuthenticationService {

    private final static String LDAP_BASE = ",OU=思必驰,OU=AISPEECH,DC=aispeech,DC=in";

    private static final Logger logger = LoggerFactory.getLogger(LdapAuthenticationServiceImpl.class);

    @Autowired
    LdapContextSource ldapContextSource;
    @Autowired
    LdapTemplate ldapTemplate;

    @Override
    public User ldapAuth(String username, String password) {
        User user = getDnFromUser(username);
        boolean result = authentication(user.getDn(),password);
        if (result) {
            return user;
        }
        return null;
    }

    @Override
    @SuppressWarnings({ "unused", "unchecked" })
    public User getDnFromUser(String username) {
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "sAMAccountName=" + username;
        List<User> result = ldapTemplate.search("", searchFilter, SearchControls.SUBTREE_SCOPE, new UserAttributesMapper());
        logger.info("get ldap user info: {}", username);
        if (result.size() != 1) {
            logger.info("user not found or not unique: {}", username);
            throw new RuntimeException("User not found or not unique");
        }
        return result.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean authentication(String userDn, String password) {
        DirContext context = null;
        userDn = userDn.replaceAll(LDAP_BASE, "");
        try {
            //调用ldap 的 authenticate方法检验
            boolean authenticate = ldapTemplate.authenticate(userDn, "(objectclass=person)", password);
            logger.info("get authenticate result: {}", authenticate);
            return authenticate;
        } catch (Exception e) {
            logger.error("authenticate failed: {}", e.getMessage());
            return false;
        } finally {
            LdapUtils.closeContext(context);
        }
    }
}
