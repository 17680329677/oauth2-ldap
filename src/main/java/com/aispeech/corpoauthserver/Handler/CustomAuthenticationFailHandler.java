package com.aispeech.corpoauthserver.Handler;

import com.aisp.horizontal.helper.JsonHelper;
import com.aispeech.corpoauthserver.Controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录失败handler
 * @Author: hezhe.du
 * @Date: 2019/6/25 0025 14:10
 */

@Component
public class CustomAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        logger.info("login failed");
        //设置状态码
        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        //将登录失败 信息打包成json格式返回
        Map<String, Object> result = new HashMap<>();
        result.put("status", 403);
        result.put("msg", "用户名密码错误！");
        response.getWriter().write(JsonHelper.stringify(result));
        response.sendRedirect("/auth/login?msg=username or password error");
    }
}
