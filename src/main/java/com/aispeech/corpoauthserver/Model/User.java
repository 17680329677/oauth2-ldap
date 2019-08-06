package com.aispeech.corpoauthserver.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/11 0011 14:05
 */

@Data
@ToString
@Entry(objectClasses = {"user"})
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 人员姓名
     */
    @Attribute(name = "cn")
    private String personName;

    /**
     * 邮箱
     */
    @Attribute(name = "email")
    private String email;

    /**
     * 用户dn
     */
    @Attribute(name = "distinguishedName")
    private String dn;

    /**
     * username
     */
    @Attribute(name = "sAMAccountName")
    private String username;

}
