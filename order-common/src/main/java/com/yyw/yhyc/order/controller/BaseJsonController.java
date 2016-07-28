package com.yyw.yhyc.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shiyongxi on 2016/7/28.
 */
public class BaseJsonController {
    private static final Logger logger = LoggerFactory.getLogger(BaseJsonController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> exceptionHandler(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage(), ex);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", ex.getMessage());
        map.put("data", null);

        if(ex.getClass() == NoSuchRequestHandlingMethodException.class)
            map.put("statusCode", 400);
        else
            map.put("statusCode", 500);

        return map;
    }
}
