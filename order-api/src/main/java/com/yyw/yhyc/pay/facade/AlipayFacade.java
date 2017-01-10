package com.yyw.yhyc.pay.facade;

import java.util.Map;

/**
 * Created by bigwing on 2016/10/25.
 */
public interface AlipayFacade {

    /**
     *  支付
     * @param out_trade_no 商户订单号，商户网站订单系统中唯一订单号，必传
     * @param subject  订单名称，必填
     * @param total_fee 付款金额，必填
     * @param body 商品描述，可传空
     */
    public String alipayCommit(String out_trade_no,String subject,String total_fee,String body);

    /**
     * 单笔退款
     * @param batch_num  总笔数
     *
     * @param trade_no   原付款支付宝交易号
     * @param total_fee  退款总金额
     * @param reason     退款理由
     */
    public String alipayrefundFastpay(int batch_num,String trade_no,String total_fee,String reason,String refundsFlowId,String businessType)  throws Exception;

    /**
     * 数据集退款
     * @param batch_num 总笔数
     * @param refundMap 交易退款数据集的单个map格式为： key = n,value="原付款支付宝交易号^退款总金额^退款理由"；
     */
    public String alipayrefundFastpayByMap(int batch_num,Map<Integer,String> refundMap,String tradeNo,String refundsFlowId,String businessType)  throws Exception;
}
