package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangshuai on 2016/9/7.
 */

@Controller
@RequestMapping(value = "/product")
public class ProductController extends BaseJsonController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Reference
    private IProductDubboManageService iProductDubboManageService;

    @RequestMapping(value = "/getProductImg", method = RequestMethod.GET)
    @ResponseBody
    private String getProductImg(@RequestParam("spuCode")  String spuCode ){
        String filePath="";
        Map map = new HashMap();
        map.put("spu_code", spuCode);
        map.put("type_id", "1");
        try{
            logger.info("查询图片接口:请求参数：map=" + map);
            List picUrlList = iProductDubboManageService.selectByTypeIdAndSPUCode(map);
            logger.info("查询图片接口:响应参数：picUrlList=" + picUrlList);
            if(UtilHelper.isEmpty(picUrlList)){
                logger.error("调用图片接口：无响应");
                return "";
            }
            JSONObject productJson = JSONObject.fromObject(picUrlList.get(0));
            filePath = (String)productJson.get("file_path");
            if (UtilHelper.isEmpty(filePath))
             return "";
        }catch (Exception e){
            logger.error("查询图片接口:调用异常," + e.getMessage());
        }
        return filePath;
    }
}
