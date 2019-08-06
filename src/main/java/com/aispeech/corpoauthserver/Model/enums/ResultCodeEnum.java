package com.aispeech.corpoauthserver.Model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/10 0010 18:29
 */

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    OK(200, "请求成功"),
    Result_Empty(201, "查询结果为空"),
    Need_Authentication(401, "需要认证"),
    Login_Failed(403, "用户名或密码错误"),
    NotExecuted(501, "命令执行异常"),
    ParamsError(101, "请求字段错误"),
    GetUserInfoFailed(102, "获取用户信息失败"),
    InvalidSignature(601, "token签名错误"),
    TokenExpires(602, "token过期"),
    TokenParseError(603, "token解析异常");

    private Integer code;

    private String msg;
}
