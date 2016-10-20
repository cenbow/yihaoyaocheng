package com.yyw.yhyc.order.controller;

import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.utils.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shiyongxi on 2016/10/20.
 */
@Controller
@RequestMapping("/cache")
public class CacheController extends BaseJsonController {
    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @RequestMapping("/flushDataCache")
    @ResponseBody
    public Map<String, Object> flushDataCache(){
        Map<String, Object> map = new HashMap<>();
        try {
            CacheUtil.getSingleton().batchDelete("MYBATIS-YHYC:");
        }catch (Exception e){
            logger.error(e.getMessage(), e);

            map.put("res", "清除失败");
            map.put("message", e.getMessage());
        }

        map.put("res", "清除成功");
        return map;
    }
}
