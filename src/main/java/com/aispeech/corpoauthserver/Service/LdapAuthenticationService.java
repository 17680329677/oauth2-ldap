package com.aispeech.corpoauthserver.Service;

import com.aispeech.corpoauthserver.Model.User;

/**
 * @Author: hezhe.du
 * @Date: 2019/7/11 0011 11:05
 */

public interface LdapAuthenticationService {

    User ldapAuth(String username, String password);

    User getDnFromUser(String username);

    boolean authentication(String userDn, String password);

}
