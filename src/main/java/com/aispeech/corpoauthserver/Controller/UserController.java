package com.aispeech.corpoauthserver.Controller;

import com.aisp.horizontal.helper.LogHelper;
import com.aispeech.corpoauthserver.Model.ResultModel;
import com.aispeech.corpoauthserver.Model.enums.ResultCodeEnum;
import com.aispeech.corpoauthserver.Utils.ResultUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/11 0011 15:34
 */

@RestController
@RequestMapping("/ie-oauth/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String SIGNING_KEY = "JWT_SIGNING_KEY";

    @Autowired
    private Environment environment;

    @GetMapping("/info")
    public ResultModel getInfo(HttpServletRequest request) {
        String authHead = request.getHeader("Authorization");
        String jwtToken = authHead.split(" ")[1];
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Secret = encoder.encodeBuffer(this.environment.getProperty(SIGNING_KEY).getBytes());
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
                    .parseClaimsJws(jwtToken)
                    .getBody();
            logger.info("get user info success: {}", claims.get("user_name"));
            return ResultUtil.success(ResultCodeEnum.OK, claims);
        } catch (SignatureException se) {
            logger.warn("signature invalid, get user info failed: {}", LogHelper.toString(se));
            return ResultUtil.error(ResultCodeEnum.InvalidSignature);
        }
    }
}
