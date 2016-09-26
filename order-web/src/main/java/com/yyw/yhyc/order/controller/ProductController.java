package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.product.service.ProductInfoService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProductInfoService productInfoService;

    @RequestMapping(value = "/getProductImg", method = RequestMethod.GET)
    @ResponseBody
    public String getProductImg(@RequestParam("spuCode")  String spuCode ){
        return  productInfoService.getProductImg(spuCode,iProductDubboManageService);


    }
}
