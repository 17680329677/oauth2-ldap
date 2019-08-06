package com.aispeech.corpoauthserver.Utils;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

import javax.naming.Name;

/**
 * @Author: hezhe.du
 * @Date: 2019/7/11 0011 11:13
 */

public class DnMapper implements ContextMapper {
    @Override
    public String mapFromContext(Object ctx) {
        DirContextAdapter context = (DirContextAdapter)ctx;
        Name name = context.getDn();
        String dn = name.toString();
        return dn;
    }
}
