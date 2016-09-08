package com.yyw.yhyc.pay.chinapay.httpClient;


import com.yyw.yhyc.pay.chinapay.utils.PayUtil;

import java.util.Map;


/**
 *
 * @author Lvhj  
 * 
 */

public class ChinaPayHttpClient {
	
	private static final String REFUND_URL="refund_url";
	
	private static final String QUERY_URL="query_url";

	
	/*
	 * 取消已支付定单
	 * 向银联发起退款请求
	 */
	protected static Map<String, String> cancelPayOrder(Map<String, Object> params) {
		params=HttpRequestHandler.getTradeRefundsMap(params);
		return post2ChinaPay(params, PayUtil.getValue(REFUND_URL));

	}
	

	/*
	 * 确认收货后，通过后台的以post的方式向银联发起支付到客户的银行卡
	 */
	protected static Map<String, String> sendPayRequest2ChinaPay(Map<String, Object> params) {
		params=HttpRequestHandler.getTradeDoneMap(params);
		return post2ChinaPay(params, PayUtil.getValue(REFUND_URL));

	}
	
	/*
	 * 通过后台的以post的方式向银联发起定单支付记录相关查询
	 */
	protected static Map<String, String> sendQueryRequest2ChinaPay(Map<String, Object> params) {
		params=HttpRequestHandler.getTradeQueryMap(params);
		return post2ChinaPay(params, PayUtil.getValue(QUERY_URL));

	}

	/*
	 * 通过后台的形式向银联请求
	 */
	protected static Map<String, String> post2ChinaPay(Map<String, Object> params, String action) {
		return HttpRequestHandler.executePostHttpRequest(params, action);
	}
	

}
