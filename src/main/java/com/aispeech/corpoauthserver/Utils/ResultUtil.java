package com.aispeech.corpoauthserver.Utils;

import com.aispeech.corpoauthserver.Model.ResultModel;
import com.aispeech.corpoauthserver.Model.enums.ResultCodeEnum;

/**
 * @Author: hezhe.du
 * @Date: 2019/6/10 0010 10:45
 */

public class ResultUtil {

    public static ResultModel success(ResultCodeEnum resultCode, Object object) {
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(resultCode.getCode());
        resultModel.setMessage(resultCode.getMsg());
        resultModel.setData(object);
        return resultModel;
    }

    public static ResultModel success(ResultCodeEnum resultCode) {
        return success(resultCode, null);
    }

    public static ResultModel error(ResultCodeEnum resultCode) {
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(resultCode.getCode());
        resultModel.setMessage(resultCode.getMsg());
        return resultModel;
    }
}
