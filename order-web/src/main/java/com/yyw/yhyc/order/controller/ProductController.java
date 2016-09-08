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

    private static final Logger logger = LoggerFactory.getLogger(ProductInventoryController.class);

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
            List picUrlList = iProductDubboManageService.selectByTypeIdAndSPUCode(map);
            if(UtilHelper.isEmpty(picUrlList)){
                logger.error("图片接口调用异常");
                return "";
            }
            JSONObject productJson = JSONObject.fromObject(picUrlList.get(0));
            filePath = (String)productJson.get("file_path");
            if (UtilHelper.isEmpty(filePath))
             return "";
        }catch (Exception e){
            logger.error("图片接口调用异常");
            logger.error(e.getMessage());
        }
        return filePath;
    }
}
