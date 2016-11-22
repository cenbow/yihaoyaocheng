package com.yyw.yhyc.order.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yaoex.framework.core.model.util.StringUtil;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderPayService;
import com.yyw.yhyc.order.utils.RandomUtil;
import com.yyw.yhyc.pay.alipay.config.AlipayConfig;
import com.yyw.yhyc.pay.alipay.util.AlipaySubmit;
import com.yyw.yhyc.pay.alipay.util.UtilDate;

/**
 * Created by yumi on 2016/11/15.
 */
@Controller
@RequestMapping(value = {"/aliappPay","/api/aliappPay","/order/api/aliappPay"})
public class AliAppPayController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(AliAppPayController.class);
	
	@Autowired
	private OrderPayService orderPayService;
    /**
     * 支付参数字符串
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/getAppPayParams")
    @ResponseBody
    public String getAppPayParams(HttpServletRequest request) throws Exception
    {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	logger.info("--- getAppPayParams start "+df.format(new Date())+" ---");
    	UserDto userDto = super.getLoginUser();
    	logger.info("--- getLoginUser  "+userDto.toString()+" ---");
		if(userDto == null || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("登陆超时");
		}
    	String trade_no = request.getParameter("out_trade_no");
    	
    	/* 支付订单前，预处理订单数据 */
		String payFlowId = RandomUtil.createOrderPayFlowId(trade_no);
		OrderPay orderPay = orderPayService.preHandler(userDto, trade_no, 8, payFlowId);
		if(UtilHelper.isEmpty(orderPay)) throw new Exception("非法参数");

    	Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service_app);
        sParaTemp.put("partner", AlipayConfig.partner_app);
        sParaTemp.put("seller_id", AlipayConfig.seller_id_app);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notify_url_app);
        sParaTemp.put("it_b_pay",AlipayConfig.it_b_pay);
        logger.info("out_trade_no : "+trade_no);
        if(StringUtil.isNotEmpty(trade_no)){
        	sParaTemp.put("out_trade_no",payFlowId);
        }else {
			return "out_trade_no is null";
		}
        logger.info("subject : "+request.getParameter("subject"));
        if(StringUtil.isNotEmpty(request.getParameter("subject"))){
        	sParaTemp.put("subject", request.getParameter("subject"));
        }else {
			return "subject is null";
		}
        logger.info("total_fee : "+request.getParameter("total_fee"));
        if(StringUtil.isNotEmpty(request.getParameter("total_fee"))){
        	sParaTemp.put("total_fee", request.getParameter("total_fee"));
        }else {
			return "total_fee is null";
		}
        logger.info("body : "+request.getParameter("body"));
        if(StringUtil.isNotEmpty(request.getParameter("body"))){
        	sParaTemp.put("body",request.getParameter("body"));
        }else {
			return "body is null";
		}
        map.put("data", AlipaySubmit.buildAppRequestUrl(sParaTemp));
    	map.put("message", "成功");
        map.put("statusCode", "0");
        String res = JSONObject.toJSONString(map, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue);
        logger.info("return res = "+res);
        logger.info("--- getAppPayParams end "+df.format(new Date())+" ---");
       return res;
    }


    /**
     * 退款参数字符串
     * @param request
     * @return
     */
    @RequestMapping(value = "/getAppRefundParams")
    @ResponseBody
    public String getAppRefundParams(HttpServletRequest request){

        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.servicerefund);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", AlipayConfig.notify_url_refund);
        sParaTemp.put("seller_user_id", AlipayConfig.seller_id);
        sParaTemp.put("refund_date", AlipayConfig.refund_date);
        sParaTemp.put("batch_no", UtilDate.getOrderNum());
        sParaTemp.put("batch_num", request.getParameter("batch_num"));
        sParaTemp.put("detail_data", request.getParameter("detail_data"));

        return AlipaySubmit.buildAppRequestUrl(sParaTemp);
    }

//    public static void main(String[] args) {
//    	Map<String, Object> map = new HashMap<String, Object>();
//        Map<String, String> sParaTemp = new HashMap<String, String>();
//        sParaTemp.put("service", AlipayConfig.service_app);
//        sParaTemp.put("partner", AlipayConfig.partner_app);
//        sParaTemp.put("seller_id", AlipayConfig.seller_id_app);
//        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
//        sParaTemp.put("payment_type", AlipayConfig.payment_type);
//        sParaTemp.put("notify_url", AlipayConfig.notify_url);
//        sParaTemp.put("return_url", AlipayConfig.return_url);
//        sParaTemp.put("it_b_pay",AlipayConfig.it_b_pay);
//    	sParaTemp.put("out_trade_no","ZXD201611111143521333");
//    	sParaTemp.put("subject", "1号药城app");
//    	sParaTemp.put("total_fee", "0.01");
//    	sParaTemp.put("body","android下单");
//        map.put("data", AlipaySubmit.buildAppRequestUrl(sParaTemp));
//    	map.put("message", "成功");
//        map.put("statusCode", "0");
//        String res = JSONObject.toJSONString(map, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue);
//        System.out.println(res);
//	}
}
