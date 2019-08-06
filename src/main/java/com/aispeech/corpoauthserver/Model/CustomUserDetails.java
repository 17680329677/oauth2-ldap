package com.aispeech.corpoauthserver.Model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 *
 * @Author: hezhe.du
 * @Date: 2019/7/11 0011 15:39
 */

@Data
public class CustomUserDetails extends User {

    private String userDn;

    private String personName;

    private String mail;

    public CustomUserDetails(String username, String password,
                             Collection<? extends GrantedAuthority> authorities, String personName, String mail, String userDn) {
        super(username, password,true, true, true, true, authorities);
        this.personName = personName;
        this.mail = mail;
        this.userDn = userDn;
    }
}
