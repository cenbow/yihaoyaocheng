package com.yyw.yhyc.order.inteceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zhangqiang on 2016/8/5.
 * 未登录拦截
 */
@Component
public class LoginInteceptor extends HandlerInterceptorAdapter {
    Logger log = LoggerFactory.getLogger(LoginInteceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        if(url.indexOf("/login") == -1 && url.indexOf("/doLogin") == -1){
            Object session = request.getSession().getAttribute("user");
            log.debug("session : "+session);
            if(session == null){
                response.sendRedirect("/login");
                return false;
            }
        }

        return true;
    }

}
