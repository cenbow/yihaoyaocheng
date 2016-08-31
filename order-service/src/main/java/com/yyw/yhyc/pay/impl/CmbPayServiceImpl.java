package com.yyw.yhyc.pay.impl;


import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.service.AccountPayInfoService;
import com.yyw.yhyc.order.service.OrderCombinedService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.pay.cmbPay.CmbPayUtil;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhou on 2016/8/30
 *  招商银行支付相关
 */
@Service("cmbPayService")
public class CmbPayServiceImpl implements PayService{

    private static final Logger log = LoggerFactory.getLogger(CmbPayServiceImpl.class);

    private OrderCombinedService orderCombinedService;
    @Autowired
    public void setOrderCombinedService(OrderCombinedService orderCombinedService) {
        this.orderCombinedService = orderCombinedService;
    }

    private OrderService orderService;
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    private AccountPayInfoService accountPayInfoService;
    @Autowired
    public void setAccountPayInfoService(AccountPayInfoService accountPayInfoService) {
        this.accountPayInfoService = accountPayInfoService;
    }

    @Override
    public Map<String, Object> postToBankForDoneOrder(Map<String, Object> orderInfo, int Action) {
        return null;
    }

    /**
     * 在发送支付请求之前，组装数据
     * @param orderPay
     * @param systemPayType
     * @return
     */
    @Override
    public Map<String, Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType) throws Exception {
        if(UtilHelper.isEmpty(orderPay) || UtilHelper.isEmpty(orderPay.getPayFlowId())){
            return null;
        }
        OrderCombined orderCombined = orderCombinedService.findByPayFlowId(orderPay.getPayFlowId());
        if(UtilHelper.isEmpty(orderCombined)) return null;

        Order orderQuery = new Order();
        orderQuery.setOrderCombinedId(orderCombined.getOrderCombinedId());
        List<Order> orderList = orderService.listByProperty(orderQuery);
        if(UtilHelper.isEmpty(orderList)) return null;

        StringBuffer orderDataInfo = new StringBuffer();
        Integer orderDataInfoCount = 0;
        AccountPayInfo accountPayInfo = null;
        for(Order order : orderList){
            accountPayInfo = new AccountPayInfo();
            accountPayInfo.setCustId(order.getSupplyId());
            accountPayInfo.setPayTypeId(order.getPayTypeId());
            List<AccountPayInfo> accountPayInfoList = accountPayInfoService.listByProperty(accountPayInfo);
            if(UtilHelper.isEmpty(accountPayInfoList) || accountPayInfoList.size() != 1){
                throw  new Exception("供应商收款账户异常");
            }
            accountPayInfo = accountPayInfoList.get(0);
            if(UtilHelper.isEmpty(accountPayInfo) || UtilHelper.isEmpty(accountPayInfo.getReceiveAccountNo())
                    || UtilHelper.isEmpty(accountPayInfo.getReceiveAccountName())){
                throw  new Exception("供应商收款账户异常");
            }

            if(orderDataInfoCount != 0){
                orderDataInfo.append("\r\n");
            }

            orderDataInfo.append( CmbPayUtil.MCHNBR + "=" + CmbPayUtil.getValue(CmbPayUtil.MCHNBR) + " ;");//商户编码
            orderDataInfo.append( CmbPayUtil.REFORD + "=" + orderPay.getPayFlowId() + " ;");//订单号
            orderDataInfo.append( CmbPayUtil.CCYNBR + "=" + CmbPayUtil.getValue(CmbPayUtil.CCYNBR) + " ;");//订单币种
            orderDataInfo.append( CmbPayUtil.TRSAMT + "=" + orderPay.getOrderMoney() + " ;");//订单金额
            orderDataInfo.append( CmbPayUtil.CRTACC + "=" + accountPayInfo.getReceiveAccountNo()  + " ;");//收方账号
            orderDataInfo.append( CmbPayUtil.CRTNAM + "=" + accountPayInfo.getReceiveAccountName() + " ;");//收方账户名
            orderDataInfo.append( CmbPayUtil.CRTBNK + "=" + accountPayInfo.getSubbankName()  + " ;");//收方开户行
//            TODO 省市区的字段如何处理？
//            orderDataInfo.append( CmbPayUtil.CRTPVC + "=" +   + " ;");//收方省份
//            orderDataInfo.append( CmbPayUtil.CRTCTY + "=" +   + " ;");//收方城市
            orderDataInfo.append( CmbPayUtil.RETURL + "=" + CmbPayUtil.getValue("payHost") + " /" + CmbPayUtil.getValue("payReturnUrl") + " ;");//同步响应URL
            orderDataInfo.append("\0");
            orderDataInfoCount++;
        }
        log.info("招商银行支付请求之前，组装数据:orderDataInfo=" + orderDataInfo);

        String sigdat = "";
        //TODO 调用生成签名接口
        log.info("招商银行支付请求之前，生成签名:sigdat=" + sigdat);

        String CMB_PAY_URL = UtilHelper.isEmpty(sigdat) ? CmbPayUtil.getValue(CmbPayUtil.CMB_PAY_URL_WITHOUT_SIGNED) : CmbPayUtil.getValue(CmbPayUtil.CMB_PAY_URL_WITH_SIGNED);

        Map<String, Object> payRequestParamMap = new HashMap<>();
        payRequestParamMap.put("order",orderDataInfo);//订单信息
        payRequestParamMap.put("sigdat", sigdat);//签名
        payRequestParamMap.put("CMB_PAY_URL",CMB_PAY_URL);
        return payRequestParamMap;
    }

    @Override
    public String paymentCallback(HttpServletRequest request) {
        log.debug("招行支付回调请求信息，request:"+request);

        return null;
    }


}
