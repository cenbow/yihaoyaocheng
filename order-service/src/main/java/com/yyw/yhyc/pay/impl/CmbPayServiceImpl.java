package com.yyw.yhyc.pay.impl;

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lizhou on 2016/8/30
 *  招商银行支付相关
 */
@Service("cmbPayService")
public class CmbPayServiceImpl implements PayService{

    @Override
    public Map<String, Object> postToBankForDoneOrder(Map<String, Object> orderInfo, int Action) {
        return null;
    }

    /**
     * 在发送支付请求之前，组装数据
     * @param orderPay
     * @return
     */
    public Map<String, Object> handleDataBeforeSendPayRequest(OrderPay orderPay) {
        return null;
    }
}
