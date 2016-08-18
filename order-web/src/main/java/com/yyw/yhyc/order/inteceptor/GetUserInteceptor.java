package com.yyw.yhyc.order.inteceptor;

import com.yhyc.auth.User;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.CustTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangqiang on 2016/8/5.
 * 获取用户信息拦截
 */
@Component
public class GetUserInteceptor extends HandlerInterceptorAdapter {
    Logger log = LoggerFactory.getLogger(GetUserInteceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            User u = (User) request.getAttribute("loginUser");
            if(!UtilHelper.isEmpty(u) && UtilHelper.isEmpty(request.getAttribute(UserDto.REQUEST_KEY))){
                UserDto userDto = new UserDto();
                userDto.setUserName(u.getUsername());
                userDto.setCustId(u.getEnterprise_id());
                userDto.setCustName(u.getEnterprise_id().toString());

                CustTypeEnum custTypeEnum = null;
                switch (u.getRole_id()){
                    case 101:
                        custTypeEnum = CustTypeEnum.buyer;
                        break;
                    case 201:
                        custTypeEnum = CustTypeEnum.seller;
                        break;
                    case 301:
                        custTypeEnum = CustTypeEnum.buyerAndSeller;
                        break;
                    default:
                        custTypeEnum = CustTypeEnum.other;
                        break;
                }
                userDto.setCustType(custTypeEnum);

                request.setAttribute(UserDto.REQUEST_KEY, userDto);

                log.info("userDto-->" + userDto.toString());
            }

            return true;
    }

}
