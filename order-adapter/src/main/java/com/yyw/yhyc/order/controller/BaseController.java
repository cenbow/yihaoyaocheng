package com.yyw.yhyc.order.controller;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhou on 2016/9/20.
 */
public class BaseController {


    /* 请求成功 */
    public static final int STATUS_CODE_REQUEST_SUCCESS = 0;

    /* 请求格式错误 */
    public static final int STATUS_CODE_REQUEST_PARAM_ERROR = -1;

    /* 非法token */
    public static final int STATUS_CODE_INVALID_TOKEN = -2;

    /* 调用业务，有异常抛出 */
    public static final int STATUS_CODE_SYSTEM_EXCEPTION = -3;


    private static final Logger logger = LoggerFactory.getLogger(com.yyw.yhyc.order.controller.BaseController.class);
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    public BaseController() {
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Map<String, Object> exceptionHandler(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage(),ex);
        HashMap map = new HashMap();
        map.put("statusCode", STATUS_CODE_SYSTEM_EXCEPTION);
        map.put("message", ex.getMessage());
        map.put("data", null);
        return map;
    }

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    public Map<String,Object> ok(Object data ){
        return ok(data,"成功");
    }

    public Map<String,Object> ok(Object data,String message ){
        return returnResult(STATUS_CODE_REQUEST_SUCCESS,message,data);
    }

    public Map<String,Object> error(int statusCode,String message){
        return returnResult(statusCode, message,null);
    }

    public Map<String,Object> error(String message){
        return returnResult(STATUS_CODE_SYSTEM_EXCEPTION, message,null);
    }

    public Map<String,Object> error(Map<String, Object> data){
        return returnResult(STATUS_CODE_SYSTEM_EXCEPTION, "服务器异常",data);
    }

    public Map<String,Object> returnResult(int statusCode,String message,Object data){
        Map<String, Object> result = new HashMap<>();
        result.put("statusCode", statusCode);
        result.put("message", message);
        result.put("data",data);
        return result;
    }

    protected <T> T getLoginUser(){
        T t = (T) session.getAttribute(UserDto.REQUEST_KEY);
        if(UtilHelper.isEmpty(t))
            t = (T) request.getAttribute(UserDto.REQUEST_KEY);
        return t;
    }
}

