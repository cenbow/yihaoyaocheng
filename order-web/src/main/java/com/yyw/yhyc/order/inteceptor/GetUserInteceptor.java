package com.yyw.yhyc.order.inteceptor;

import com.alibaba.fastjson.JSONObject;
import com.yhyc.auth.User;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.CustTypeEnum;
import com.yyw.yhyc.utils.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 2016/8/5.
 * 获取用户信息拦截
 */
public class GetUserInteceptor extends HandlerInterceptorAdapter {
    Logger log = LoggerFactory.getLogger(GetUserInteceptor.class);

    public static final String CACHE_PREFIX = "passport";

    public static final String COMMON_INFO = "commoninfo";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            User u = (User) request.getAttribute("loginUser");
            if(!UtilHelper.isEmpty(u) && UtilHelper.isEmpty(request.getAttribute(UserDto.REQUEST_KEY))){
                String commonInfo = CacheUtil.getSingleton().get(CACHE_PREFIX + COMMON_INFO + request.getSession().getId());
                String user = CacheUtil.getSingleton().get(CACHE_PREFIX + request.getSession().getId());

                log.info("user-->" + user);
                log.info("commonInfo-->" + commonInfo);

                UserDto userDto = new UserDto();
                userDto.setUserName(u.getUsername());
                userDto.setCustId(u.getEnterprise_id());

                //用户信息
                if(!UtilHelper.isEmpty(user))
                {
                    Map userMap = JSONObject.parseObject(user, HashMap.class);
                    userDto.setUser(userMap);
                }

                //企业信息
                if(!UtilHelper.isEmpty(commonInfo)){
                    Map commonMap = JSONObject.parseObject(commonInfo, HashMap.class);

                    userDto.setCustName(this.getMapToString(commonMap, "enterpriseName"));
                    userDto.setProvince(this.getMapToString(commonMap, "province"));
                    userDto.setProvinceName(this.getMapToString(commonMap, "provinceName"));
                    userDto.setCity(this.getMapToString(commonMap, "city"));
                    userDto.setCityName(this.getMapToString(commonMap, "cityName"));
                    userDto.setDistrict(this.getMapToString(commonMap, "district"));
                    userDto.setDistrictName(this.getMapToString(commonMap, "districtName"));
                    userDto.setRegisteredAddress(this.getMapToString(commonMap, "registeredAddress"));

                    log.debug("+++++++++++++++++++++++++++++++++++++++++++");
                    log.debug(commonMap.toString());
                    log.debug(this.getMapToString(commonMap, "roleType").getClass().getName());
                    log.debug(this.getMapToString(commonMap, "roleType"));

                    String roleType = this.getMapToString(commonMap, "roleType");
                    if(!UtilHelper.isEmpty(roleType)){
                        CustTypeEnum custTypeEnum = null;
                        switch (roleType){
                            case "1":
                                custTypeEnum = CustTypeEnum.buyer;
                                break;
                            case "2":
                                custTypeEnum = CustTypeEnum.seller;
                                break;
                            case "3":
                                custTypeEnum = CustTypeEnum.buyerAndSeller;
                                break;
                            default:
                                custTypeEnum = CustTypeEnum.other;
                                break;
                        }
                        userDto.setCustType(custTypeEnum);
                    }
                }

                request.setAttribute(UserDto.REQUEST_KEY, userDto);

                log.info("userDto-->" + userDto.toString());
            }
            log.info("current user-->" + (UserDto)request.getAttribute(UserDto.REQUEST_KEY));
            return true;
    }

    private String getMapToString(Map map, String key){
        if(UtilHelper.isEmpty(map) || UtilHelper.isEmpty(map.get(key)))
            return "";
        else
            return map.get(key).toString();
    }
}
