package com.aispeech.corpoauthserver.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/10 0010 10:38
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultModel<T> {

    /** API状态码 */
    private Integer code;

    /** API状态码信息 */
    private String message;

    /** API返回的对象 */
    private T data;
}
