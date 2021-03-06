<%
/* *
 功能：支付宝服务器异步通知页面
 版本：3.3
 日期：2012-08-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayNotify.java类文件中
 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.yyw.yhyc.helper.SpringBeanHelper"%>
<%@ page import="com.yyw.yhyc.order.manage.OrderPayManage"%>
<%@ page import="com.yyw.yhyc.order.service.OrderSettlementService"%>
<%@ page import="com.yyw.yhyc.pay.alipay.util.AlipayNotify" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.slf4j.Logger" %>

<%!
	private static final Logger logger = LoggerFactory.getLogger("jsp.orderPay.alipay_notify_refund.jsp");
%>

<%
	//获取支付宝POST过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map requestParams = request.getParameterMap();
	logger.error("支付宝退款：退款成功后的，处理支付宝的回调信息......原始信息：requestParams = " + requestParams);
	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
		params.put(name, valueStr);
	}
	logger.info("支付宝退款：退款成功后的，处理支付宝的回调信息......转码后的信息：params =" + params);
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	//批次号
	String batch_no = new String(request.getParameter("batch_no").getBytes("ISO-8859-1"),"UTF-8");
	//批量退款数据中转账成功的笔数
	String success_num = new String(request.getParameter("success_num").getBytes("ISO-8859-1"),"UTF-8");
	//批量退款数据中的详细信息
	String result_details = new String(request.getParameter("result_details").getBytes("ISO-8859-1"),"UTF-8");

	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
	boolean result = AlipayNotify.verify(params);
	logger.error("支付宝退款：退款成功后的，处理支付宝的回调信息......校验签名的结果=" + result );
	if(AlipayNotify.verify(params)){//验证成功
		logger.info("支付宝退款：退款成功后的，处理支付宝的回调信息......校验签名通过！" );
		//////////////////////////////////////////////////////////////////////////////////////////
		//请在这里加上商户的业务逻辑程序代码

		//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
		OrderPayManage orderPayManage = (OrderPayManage) SpringBeanHelper.getBean("orderPayMamage");
		OrderSettlementService orderSettlementService = (OrderSettlementService) SpringBeanHelper.getBean("orderSettlementService");
		try {
			String detail =null;
			String[] resultdetail = result_details.split("#");
			logger.error("result_details===="+result_details);
			for(String tempreturn : resultdetail)
			{
				if(tempreturn.indexOf("$") == -1)
				{
					detail = tempreturn;
				}
				else
				{
					detail = tempreturn.split("\\$")[0];
				}
				String tradeNo = AlipayNotify.getTradeNo(detail);
				logger.error("tradeNo===="+tradeNo);
				String paymentPlatforReturn = orderPayManage.getPayFlowIdByPayAccountNo(tradeNo);
				logger.error("paymentPlatforReturn===="+paymentPlatforReturn);
				Map<String, String> myMap = new HashMap<String, String>();

				String[] pairs = paymentPlatforReturn.split(",");
				for (int i=0;i<pairs.length;i++) {
					String pair = pairs[i];
					String[] keyValue = pair.split("=");
					myMap.put(keyValue[0].trim().toString(), keyValue[1]);
				}

				String temp = myMap.get("subject").toString();
				logger.info("支付宝App退款：退款成功后的，处理支付宝的回调信息......flowId=" + temp );
				orderSettlementService.updateSettlementByMapInfo(temp,tradeNo);
				Boolean state = AlipayNotify.getIsSuccess(detail);
				if(state)
					orderSettlementService.updateConfirmSettlement(tradeNo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("支付宝退款：退款成功后的，处理支付宝的回调信息......回写支付信息到数据库发生异常....异常信息:"+e.getMessage(),e);
		}
		//判断是否在商户网站中已经做过了这次通知返回的处理
			//如果没有做过处理，那么执行商户的业务程序
			//如果有做过处理，那么不执行商户的业务程序
			
		logger.error("支付宝退款：退款成功后的，处理支付宝的回调信息......处理支付宝的回调信息请求完成！");
		out.print("success");	//请不要修改或删除
		//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

		//////////////////////////////////////////////////////////////////////////////////////////
	}else{//验证失败
		logger.info("支付宝退款：退款成功后的，处理支付宝的回调信息......校验签名失败......" );
		out.print("fail");
	}
%>