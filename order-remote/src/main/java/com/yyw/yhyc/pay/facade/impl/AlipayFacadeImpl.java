package com.yyw.yhyc.pay.facade.impl;

import com.yyw.yhyc.pay.alipay.config.AlipayConfig;
import com.yyw.yhyc.pay.alipay.util.AlipayCore;
import com.yyw.yhyc.pay.alipay.util.UtilDate;
import com.yyw.yhyc.pay.facade.AlipayFacade;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bigwing on 2016/10/26.
 */
public class AlipayFacadeImpl implements AlipayFacade {

    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

    @Override
    public void alipayCommit(String out_trade_no, String subject, String total_fee, String body) {

        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(ALIPAY_GATEWAY_NEW);

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

        method.setQueryString(AlipayCore.createLinkString(sParaTemp));
        try {
            client.executeMethod(method);
            System.out.println(method.getStatusCode());
            System.out.println(method.getResponseBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void alipayrefundFastpay(int batch_num,String trade_no,String total_fee,String reason) {
        Map<Integer, String> refundMap = new HashMap<>();
        refundMap.put(1,trade_no+"^"+total_fee+"^"+reason);
        alipayrefundFastpayByMap(batch_num,refundMap);
    }

    @Override
    public void alipayrefundFastpayByMap(int batch_num, Map<Integer, String> refundMap) {


        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(ALIPAY_GATEWAY_NEW);

        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", AlipayConfig.notify_url);
        sParaTemp.put("seller_user_id", AlipayConfig.seller_id);
        sParaTemp.put("refund_date", AlipayConfig.refund_date);
        sParaTemp.put("batch_no", UtilDate.getOrderNum());
        sParaTemp.put("batch_num", String.valueOf(batch_num));
        sParaTemp.put("detail_data", AlipayCore.createLinkStringByrefund(refundMap));

        method.setQueryString(AlipayCore.createLinkString(sParaTemp));
        try {
            client.executeMethod(method);
            System.out.println(method.getStatusCode());
            System.out.println(method.getResponseBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
//        AlipayFacadeImpl af = new AlipayFacadeImpl();
//        af.alipayCommit(null,null,null,null);

    }
}
