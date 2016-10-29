package com.yyw.yhyc.pay.facade.impl;

import com.yyw.yhyc.pay.alipay.config.AlipayConfig;
import com.yyw.yhyc.pay.alipay.util.AlipayCore;
import com.yyw.yhyc.pay.alipay.util.AlipaySubmit;
import com.yyw.yhyc.pay.alipay.util.UtilDate;
import com.yyw.yhyc.pay.facade.AlipayFacade;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bigwing on 2016/10/26.
 */
public class AlipayFacadeImpl implements AlipayFacade {


    @Override
    public String alipayCommit(String out_trade_no, String subject, String total_fee, String body) {

        StringBuffer response = new StringBuffer();
//        HttpClient client = new HttpClient();
//        GetMethod method = new GetMethod(ALIPAY_GATEWAY_NEW);
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notify_url);
        sParaTemp.put("return_url", AlipayConfig.return_url);
        sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("body", body);



        String sHtmlText = AlipaySubmit.buildRequestUrl(sParaTemp);


//        method.setQueryString(AlipayCore.createLinkString(sParaTemp));
//        try {
//            client.executeMethod(method);
//            System.out.println(method.getStatusCode());
//            System.out.println(method.getResponseBodyAsString());
//            method.releaseConnection();
//            System.out.println(StringUtils.repeat("=", 40));
//            System.out.println(method.getURI());
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println(sHtmlText);
        return sHtmlText;

    }

    @Override
    public String alipayrefundFastpay(int batch_num, String trade_no, String total_fee, String reason) {
        Map<Integer, String> refundMap = new HashMap<>();
        refundMap.put(1, trade_no + "^" + total_fee + "^" + reason);
        return alipayrefundFastpayByMap(batch_num, refundMap);
    }

    @Override
    public String alipayrefundFastpayByMap(int batch_num, Map<Integer, String> refundMap) {


        StringBuffer response = new StringBuffer();


        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.servicerefund);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", AlipayConfig.notify_url_refund);
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

    public static void main(String[] args) {
        AlipayFacadeImpl af = new AlipayFacadeImpl();
        af.alipayCommit("test20161029174446","test商品123","0.01","即时到账测试");
        System.out.println(StringUtils.repeat("=", 40));
        af.alipayrefundFastpay(1,"2011011201037066","0.01","协商退款");
        Map<Integer, String> refundMap = new HashMap<>();
        refundMap.put(1, "2011011201037066^0.01^协商退款");
        refundMap.put(2, "2011011201037068^0.01^协商退款");
        af.alipayrefundFastpayByMap(2,refundMap);
    }
}
