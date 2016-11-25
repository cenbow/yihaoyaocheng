package com.yyw.yhyc.order.inteceptor;

import com.alibaba.fastjson.JSONObject;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.CustTypeEnum;
import com.yyw.yhyc.order.utils.ContextHolder;
import com.yyw.yhyc.utils.CacheUtil;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shiyongxi on 2016/9/21.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = Logger.getLogger(LoginInterceptor.class);
    public static final String LOGINTYPE = "logintype";
    public static final String PASSPORT = "passport";
    public static final String COMMON_INFO = "commoninfo";
    public static final String HOUTAITYPE = "3";
    public static final String TOKEN = "token";

    public LoginInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sid = request.getHeader(TOKEN);
        if(UtilHelper.isEmpty(sid) && !isApp(request))
            sid = request.getSession().getId();

        return this.jump(request, response, handler, sid);
    }

    private boolean jump(HttpServletRequest request, HttpServletResponse response, Object handler, String sid) throws Exception {
        Map<String, Object> user  = UtilHelper.isEmpty(sid) ? null : this.getInfo(PASSPORT, sid);
        if(!UtilHelper.isEmpty(user) && !UtilHelper.isEmpty(user.get("validate")) && Boolean.valueOf(user.get("validate").toString())) {
            request.setAttribute("loginUser", user);

            Map<String, Object> commonMap = getInfo(COMMON_INFO, sid);

            logger.info("user-->" + user);
            logger.info("commonInfo-->" + commonMap);

            UserDto userDto = new UserDto();
            userDto.setUserName(getString(user, "username"));
            userDto.setCustId(getInt(user, "enterprise_id"));
            userDto.setUser(user);


            //企业信息
            if(!UtilHelper.isEmpty(commonMap)){
                userDto.setCustName(this.getString(commonMap, "enterpriseName"));
                userDto.setProvince(this.getString(commonMap, "province"));
                userDto.setProvinceName(this.getString(commonMap, "provinceName"));
                userDto.setCity(this.getString(commonMap, "city"));
                userDto.setCityName(this.getString(commonMap, "cityName"));
                userDto.setDistrict(this.getString(commonMap, "district"));
                userDto.setDistrictName(this.getString(commonMap, "districtName"));
                userDto.setRegisteredAddress(this.getString(commonMap, "registeredAddress"));

                logger.debug("+++++++++++++++++++++++++++++++++++++++++++");
                logger.debug(commonMap.toString());
                logger.debug(this.getString(commonMap, "roleType").getClass().getName());
                logger.debug(this.getString(commonMap, "roleType"));

                String roleType = this.getString(commonMap, "roleType");
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
            
             ContextHolder.setUserDto(userDto);

            logger.info("userDto-->" + userDto.toString());

            return true;
        } else {
            responseOutWithJson(response, "-2", "【" + request.getRequestURI() + "】接口未登录不可访问！");

            return false;
        }
    }

    private Map<String, Object> getInfo(String key, String sid) {
        String userJson = CacheUtil.getSingleton().get(key + sid);
        Map<String, Object> map = null;
        if(userJson != null) {
            map = JSONObject.parseObject(userJson, HashMap.class);
        }

        return map;
    }

    /**
     * 响应设置
     * @param response
     * @param statusCode
     * @param message
     */
    private void responseOutWithJson(ServletResponse response, String statusCode, String message){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(getError(statusCode, message));
            logger.debug(message);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 响应数据组装JSON格式
     * @param statusCode
     * @param message
     * @return
     */
    @SuppressWarnings("unchecked")
    private String getError(String statusCode, String message) {
        JSONObject obj = new JSONObject();
        obj.put("statusCode", statusCode);
        obj.put("data", null);
        obj.put("message", message);
        return obj.toJSONString();
    }

    /**
     * 获取String值
     * @param map
     * @param key
     * @return
     */
    private String getString(Map<String, Object> map, String key){
        if(UtilHelper.isEmpty(map)) return "";

        Object value = map.get(key);
        if(UtilHelper.isEmpty(value))
            return "";
        else
            return value.toString();
    }

    /**
     * 获取Int值
     * @param map
     * @param key
     * @return
     */
    private int getInt(Map<String, Object> map, String key){
        if(UtilHelper.isEmpty(map)) return 0;

        Object value = map.get(key);
        if(UtilHelper.isEmpty(value))
            return 0;
        else
            return Integer.parseInt(value.toString());
    }

    /**
     * 判断是否是移动端
     * @param request
     * @return
     */
    private boolean isApp(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        String[] apps = {"iphone", "ipad", "android", "linux"};

        for (String o: apps) {
            if(userAgent.toLowerCase().indexOf(o) > 0)
                return true;
        }

        return false;
    }
}
