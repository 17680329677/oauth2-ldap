package com.aispeech.corpoauthserver.Controller;

import com.aispeech.corpoauthserver.Model.CustomUserDetails;
import com.aispeech.corpoauthserver.Model.ResultModel;
import com.aispeech.corpoauthserver.Model.enums.ResultCodeEnum;
import com.aispeech.corpoauthserver.Utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hezhe.du
 * @Date: 2019/7/5 0005 17:10
 */


@Controller
@RequestMapping("/ie-oauth")  //TODO 这里只能手动修改
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static final String OAUTH_PREFIX = "URL_PREFIX";

    @Resource
    private DataSource dataSource;
    @Autowired
    private Environment environment;

    /**
     * 自定义登录页面所使用的endPoint
     * @param model
     * @return
     */
    @GetMapping("/auth/login")
    public String loginPage(Model model, HttpServletRequest request){
        String msg = request.getParameter("msg");
        if (msg != null && !"".equals(msg)) {
            model.addAttribute("msg", msg);
        }
        model.addAttribute("loginProcessUrl", environment.getProperty(OAUTH_PREFIX) + "/user/login");
        return "login";
    }

    /**
     * 自定义授权页面所使用的endPoint
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation(Model model, HttpServletRequest request) throws Exception {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        ClientDetails details = jdbcClientDetailsService.loadClientByClientId(request.getParameter("client_id"));
        if (null == details) {
            logger.warn("authorization page can not find such a client: {}", request.getParameter("client_id"));
            throw new NoSuchClientException("no such a client");
        }
        Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails)authInfo.getPrincipal();
        model.addAttribute("scopeList", details.getScope());
        model.addAttribute("clientInfo", details.getAdditionalInformation());
        model.addAttribute("urlPrefix", environment.getProperty(OAUTH_PREFIX, ""));
        model.addAttribute("userName", user.getPersonName());
        return "grant";
    }

    /**
     * 自定义回调接口
     * @param code
     * @return
     */
    @GetMapping("/auth_process")
    @ResponseBody
    public ResultModel authProcess(@RequestParam("code")String code) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        return ResultUtil.success(ResultCodeEnum.OK, data);
    }
}
