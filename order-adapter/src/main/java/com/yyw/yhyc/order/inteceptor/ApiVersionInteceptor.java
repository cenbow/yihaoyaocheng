package com.yyw.yhyc.order.inteceptor;

import com.yyw.yhyc.helper.SpringBeanHelper;
import com.yyw.yhyc.helper.UtilHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiyongxi on 2016/8/19.
 */
public class ApiVersionInteceptor  extends HandlerInterceptorAdapter {
    Logger log = LoggerFactory.getLogger(HandlerInterceptorAdapter.class);
    // 路径中版本的前缀， 这里用 /v[1-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
    private final static Pattern MAPPING_METHOD_PATTERN = Pattern.compile("\\{\\[(.+)\\],methods");

    private RequestMappingHandlerMapping handlerMapping;
    private Map<String, List<Integer>> mappingMap = new HashMap<String, List<Integer>>();

    private ApiVersionInteceptor(){
        if(handlerMapping == null)
            handlerMapping = (RequestMappingHandlerMapping) SpringBeanHelper.getBean("handlerMapping");

        getMappingMap();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String key = getKey(path);
        int version = getVersion(path);

        if(mappingMap.containsKey(key) && !mappingMap.get(key).contains(version)){
            int sendVersion = 0;
            for (int v : mappingMap.get(key)) {
                if (v > sendVersion && v < version)
                    sendVersion = v;
            }

            if(sendVersion > 0)
                request.getRequestDispatcher(key.replace("/v" + version + "/", "/v" + sendVersion + "/")).forward(request, response);
        }

        return true;
    }

    private void getMappingMap(){
        Map map =  this.handlerMapping.getHandlerMethods();
        Iterator<?> iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            log.info(entry.getKey() +":" + entry.getValue());

            putMap(entry.getKey().toString());
        }
    }

    private int getVersion(String path){
        Matcher m = VERSION_PREFIX_PATTERN.matcher(path);
        if(m.find()){
            Integer version = Integer.valueOf(m.group(1));
            return version;
        }

        return 0;
    }

    private String getKey(String path){
        if(UtilHelper.isEmpty(path)) return "";

        return path.trim().replaceAll("/v[0-9]+/", "/\\$\\{version\\}/");
    }

    private void putMap(String key){
        Matcher m = MAPPING_METHOD_PATTERN.matcher(key);
        String path = "";
        if(m.find()){
            path = m.group(1);
        }

        String[] paths = path.split("\\|\\|");

        for (String s : paths) {
            int v = getVersion(s.trim());

            if(v > 0){
                String k = getKey(s);

                List<Integer> values = null;
                if(mappingMap.containsKey(k)){
                    values = mappingMap.get(k);
                }else {
                    values = new ArrayList<Integer>();
                }

                if(values.contains(v)) continue;

                values.add(v);
                mappingMap.put(k, values);
            }
        }
    }
}
