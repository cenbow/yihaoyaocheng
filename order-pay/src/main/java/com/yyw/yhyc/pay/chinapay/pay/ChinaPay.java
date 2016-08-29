package com.yyw.yhyc.pay.chinapay.pay;

import com.yyw.yhyc.pay.chinapay.httpClient.ChinaPayHttpClient;

import java.util.Map;

/*
 * lvhj 
 * 实现接口 1 退款  2 确认收货并打款  3 提供查询查询接口
 */
public class ChinaPay extends ChinaPayHttpClient {
	
	/*
	 * 取消已支付定单
	 * 向银联发起退款请求
	 */
	public  Map<String, String> cancelOrder(Map<String, Object> params) {
		return cancelPayOrder(params);

	}
	/*
	 * 确认收货后，通过后台的以post的方式向银联发起支付到客户的银行卡
	 */
	public  Map<String, String> sendPay2ChinaPay(Map<String, Object> params) {
		return sendPayRequest2ChinaPay(params);

	}
	
	/*
	 * 通过后台的以post的方式向银联发起定单支付记录相关查询
	 */
	public  Map<String, String> sendQuery2ChinaPay(Map<String, Object> params) {
		return sendQueryRequest2ChinaPay(params);

	}

}
