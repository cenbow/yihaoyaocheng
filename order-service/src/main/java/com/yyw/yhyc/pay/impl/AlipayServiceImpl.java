package com.yyw.yhyc.pay.impl;

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
}
