package com.yyw.yhyc.pay.impl;

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.pay.alipay.config.AlipayConfig;
import com.yyw.yhyc.pay.alipay.util.AlipayCore;
import com.yyw.yhyc.pay.alipay.util.AlipaySubmit;
import com.yyw.yhyc.pay.alipay.util.UtilDate;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhou on 2016/10/29.
 */
@Service("aliPayService")
public class AlipayServiceImpl  implements PayService {

    private static final Logger log = LoggerFactory.getLogger(AlipayServiceImpl.class);

    @Override
    public Map<String, Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType, int type) throws Exception {
        //TODO 处理请求支付的参数，返回请求支付的url/已经签名过的数据，方便后续直接发起请求

        return null;
    }

    @Override
    public String paymentCallback(HttpServletRequest request) {
        return null;
    }

    @Override
    public Map<String, String> paymentOfAccountCallback(HttpServletRequest request) {
        return null;
    }

    @Override
    public String spiltPaymentCallback(HttpServletRequest request) {
        return null;
    }

    @Override
    public String redundCallBack(HttpServletRequest request) {
        return null;
    }

    @Override
    public void handleRefund(UserDto userDto, int orderType, String flowId, String refundDesc) {

    }

    @Override
    public boolean confirmReceivedOrder(String payFlowId) throws Exception {
        return false;
    }

    @Override
    public boolean cancelOrder(String payFlowId) throws Exception {
        return false;
    }

    @Override
    public String alipayCommit(String out_trade_no, String subject, String total_fee, String body) {


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

        log.info(sHtmlText);
        System.out.println(sHtmlText);
        return sHtmlText;

    }

    @Override
    public String alipayrefundFastpayByMap(int batch_num, Map<Integer, String> refundMap) {

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

        log.info(sHtmlText);
        System.out.println(StringUtils.repeat("=", 40));
        System.out.println(sHtmlText);
        return sHtmlText;
    }

}
