package com.aispeech.corpoauthserver.Handler;

import com.aisp.horizontal.helper.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录成功handler
 * @Author: hezhe.du
 * @Date: 2019/6/25 0025 14:16
 */

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功: {}", authentication.getDetails());

        RequestCache cache = new HttpSessionRequestCache();
        SavedRequest savedRequest = cache.getRequest(request, response);
        String[] clientIds = savedRequest.getParameterValues("client_id");
        if (clientIds.length > 0) {
            try {
                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientIds[0]);
                if (null == clientDetails) {
                    logger.info("No such a client: {}", clientIds[0]);
                }
            } catch (Exception e) {
                logger.error(" authentication success handler error: {}", LogHelper.toString(e));
                e.printStackTrace();
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
