package com.yyw.yhyc.pay.facade.impl;

import com.yyw.yhyc.pay.alipay.config.AlipayConfig;
import com.yyw.yhyc.pay.alipay.util.AlipayCore;
import com.yyw.yhyc.pay.alipay.util.AlipaySubmit;
import com.yyw.yhyc.pay.alipay.util.UtilDate;
import com.yyw.yhyc.pay.facade.AlipayFacade;
import com.yyw.yhyc.pay.impl.AlipayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bigwing on 2016/10/26.
 */
@Service("alipayFacade")
public class AlipayFacadeImpl implements AlipayFacade {

    private static final Logger logger = LoggerFactory.getLogger(AlipayFacadeImpl.class);

    @Autowired
    private AlipayServiceImpl alipayService;

    @Override
    public String alipayCommit(String out_trade_no, String subject, String total_fee, String body) {
        logger.info("go  to   alipayCommit");
      return alipayService.alipayCommit(out_trade_no,subject,total_fee,body);

    }

    @Override
    public String alipayrefundFastpay(int batch_num, String trade_no, String total_fee, String reason) {
        logger.info("go  to   alipayrefundFastpay");
        Map<Integer, String> refundMap = new HashMap<>();
        refundMap.put(1, trade_no + "^" + total_fee + "^" + reason);
        return alipayrefundFastpayByMap(batch_num, refundMap);
    }

    @Override
    public String alipayrefundFastpayByMap(int batch_num, Map<Integer, String> refundMap) {
        logger.info("go  to   alipayrefundFastpayByMap");
        String url_str = alipayService.alipayrefundFastpayByMap(batch_num,refundMap);
        logger.info("url_str==== "+url_str);
        return url_str;
    }


    //测试
    public static void main(String[] args) {

        Map<Integer, String> refundMap = new HashMap<>();
        refundMap.put(1, "2016110221001004600268318252^0.01^协商退款");
        refund(1,refundMap);


//        payall("test20161102210","贝尔金护腕式","0.11","美国专业护腕鼠标垫");


    }

    public static String refund(int batch_num, Map<Integer, String> refundMap)
    {
        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.servicerefund);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", "http://tpay.yaoex.com/jsp/orderPay/alipay_notify_url_refund.jsp");

        sParaTemp.put("seller_user_id", AlipayConfig.seller_id);
        sParaTemp.put("refund_date", AlipayConfig.refund_date);
        sParaTemp.put("batch_no", UtilDate.getOrderNum());
        sParaTemp.put("batch_num", String.valueOf(batch_num));
        sParaTemp.put("detail_data", AlipayCore.createLinkStringByrefund(refundMap));
        System.out.println(AlipayCore.createLinkStringByrefund(refundMap));
        String sHtmlText = AlipaySubmit.buildRequestUrl(sParaTemp);
        System.out.println(StringUtils.repeat("=", 40));
        System.out.println(sHtmlText);
        return sHtmlText;
    }


    public static String payall(String out_trade_no, String subject, String total_fee, String body)
    {
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", "http://tpay.yaoex.com/jsp/orderPay/alipay_notify_url.jsp");
        sParaTemp.put("return_url", "http://tpay.yaoex.com/jsp/orderPay/alipay_return_url.jsp");
        sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("body", body);

        String sHtmlText = AlipaySubmit.buildRequestUrl(sParaTemp);

        System.out.println(sHtmlText);
        return sHtmlText;
    }
}
