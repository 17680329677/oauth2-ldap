package com.aispeech.corpoauthserver.Utils;

import com.aispeech.corpoauthserver.Model.User;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * @Author: hezhe.du
 * @Date: 2019/7/11 0011 14:50
 */

public class UserAttributesMapper implements AttributesMapper<User> {
    @Override
    public User mapFromAttributes(Attributes attributes) throws NamingException {
        User user = new User();
        user.setPersonName((String)attributes.get("cn").get());
        user.setEmail((String)attributes.get("mail").get());
        user.setDn((String)attributes.get("distinguishedName").get());
        user.setUsername((String)attributes.get("sAMAccountName").get());
        return user;
    }
}
