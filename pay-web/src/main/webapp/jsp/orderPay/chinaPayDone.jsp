<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*,java.text.SimpleDateFormat"%>
<%@page import="com.yyw.yhyc.pay.chinapay.pay.*"%>
<%@page import="com.yyw.yhyc.pay.chinapay.utils.*"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.yyw.yhyc.helper.UtilHelper" %>
<%@ page import="com.yyw.yhyc.helper.SpringBeanHelper" %>
<%@ page import="com.yyw.yhyc.order.manage.OrderPayManage" %>
<%@ page import="org.springframework.http.HttpMethod" %>
<%@ page import="java.math.BigDecimal" %>
<%!
	private static final Logger logger = LoggerFactory.getLogger("jsp.orderPay.chinaPayDone.jsp");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<br>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>1号药城-分账退款</title>
</head>
<body>
<form name="payment"  method="POST" target="_blank">

<%
String method = request.getMethod();
if ( HttpMethod.POST.toString().equals(method) ) {
	String passwords = request.getParameter("passwords");
	if( UtilHelper.isEmpty(passwords ) || !"fkyqazsa2016".equals(passwords) ) {
		logger.error("1号药城-银联分账退款(手动操作)-----提示：请输入 正确的操作密码。。。。。。。。。。");
		return;
	}
}


Map<String, Object> sendMap = new HashMap<String, Object>();
String OriOrderNo = request.getParameter("OriOrderNo"); //系统内部的 支付流水号

SimpleDateFormat datefomet = new SimpleDateFormat("yyyyMMdd,HHmmss");
SimpleDateFormat fomet = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
Date date = new Date();
String fDate = datefomet.format(date);

if( !UtilHelper.isEmpty(OriOrderNo)) {
	logger.info("———————————————————————— [ 银联支付----手动分账 ] 处理开始————————————————————————————————————");
	OrderPayManage orderPayManage = (OrderPayManage) SpringBeanHelper.getBean("orderPayMamage");

	OriOrderNo = OriOrderNo.trim();
	sendMap.put("MerOrderNo", request.getParameter("OriOrderNo").trim()+"FZ"+request.getParameter("num"));//确认收货定单号 需要传输，订单号规则     原父定单号+01
	sendMap.put("TranDate",fDate.split(",")[0]);
	sendMap.put("TranTime", fDate.split(",")[1]);
	sendMap.put("OriTranDate", request.getParameter("OriTranDate").trim());//原定单交易日期 需要传输
	sendMap.put("OrderAmt", request.getParameter("OrderAmt").trim());//定单金额，需要转过来
	sendMap.put("OriOrderNo", OriOrderNo);//系统内部的 支付流水号
	sendMap.put("fromWhere", request.getParameter("fromWhere").trim());
	// 返回参数请参考 (新一代商户接入手册V2.1-) 后续类交易接口 的异步返回报文章
	sendMap.put("MerBgUrl", "http://www.fangkuaiyi.com/ConfirmCallBack.action");//不需要转过来
	sendMap.put("MerSplitMsg", request.getParameter("MerSplitMsg").trim());//分账信息，需要传输过来

	/* 向银联发送分账请求 */
	logger.info(" [ 银联支付----手动分账 ] 向银联发送 分账 请求，请求参数 sendMap = " + sendMap);
	Map<String, String> r = new ChinaPay().sendPay2ChinaPay(sendMap);
	logger.info(" [ 银联支付----手动分账 ] 向银联发送 分账 请求，响应参数 r = " + r);
	if (UtilHelper.isEmpty(r)) {
		logger.error(" [ 银联支付----手动分账 ] 向银联发送 分账 请求后，银联无响应!!");
		return;
	}
	%>
	分账操作后，响应结果 ：</br>
	<%
	for ( Map.Entry<String, String> entry : r.entrySet()) {
	%>
		<%=entry.getKey() %> ：<input type="text" name = '<%=entry.getKey() %>' value ='<%=entry.getValue()%>'/></br>
	<%
	}




	/* 如果有退款的情况，手动操作退款 */
	if ( !UtilHelper.isEmpty(request.getParameter("ReMerSplitMsg")) && !UtilHelper.isEmpty(request.getParameter("RefundAmt"))) {
		logger.info("\n\n\n———————————————————————— [ 银联支付----手动退款 ] 处理开始————————————————————————————————————");
		sendMap = new HashMap<String, Object>();
		sendMap.put("OriOrderNo", OriOrderNo);//系统内部的 支付流水号
		sendMap.put("OriTranDate",request.getParameter("OriTranDate").trim());//原定单交易日期 需要传输
		sendMap.put("TranDate",fDate.split(",")[0]);
		sendMap.put("TranTime", fDate.split(",")[1]);
		sendMap.put("MerOrderNo",OriOrderNo + "TK");//分账信息，需要传输过来
		sendMap.put("MerSplitMsg",request.getParameter("ReMerSplitMsg").trim());//分账信息，需要传输过来
		sendMap.put("RefundAmt",request.getParameter("RefundAmt").trim());//分账信息，需要传输过来
		sendMap.put("fromWhere", request.getParameter("fromWhere").trim());
		sendMap.put("MerBgUrl", "http://www.fangkuaiyi.com/RedundCallBack.action");//要转过来,成功后会返回应答

		logger.info(" [ 银联支付----手动退款 ] 向银联发送 退款 请求，请求参数 sendMap = " + sendMap);
		Map<String, String> rs = new ChinaPay().cancelOrder(sendMap);
		logger.info(" [ 银联支付----手动退款 ] 向银联发送 退款 请求，响应参数 rs = " + rs);

		if (UtilHelper.isEmpty(rs)) {
			logger.error(" [ 银联支付----手动退款 ] 向银联发送 退款 请求后，银联无响应!!");
			return;
		}
		%>
		</br></br>退款操作后，响应结果  ：</br>
		<%
		for(Map.Entry<String, String> entry:rs.entrySet()){
		%>
			<%=entry.getKey() %>  ：<input type="text" name = '<%=entry.getKey() %>' value ='<%=entry.getValue()%>'/></br>
		<%
		}

		try {
			logger.info(" [ 银联支付----手动退款 ] 发送 退款 请求成功后，进行相关操作");
			BigDecimal refundAmt = new BigDecimal(request.getParameter("RefundAmt").trim());
			if ("1003".equals(rs.get("respCode")) || "0000".equals(rs.get("respCode")) || "2034".equals(rs.get("respCode"))) {
				orderPayManage.updateRedundOrderInfos(OriOrderNo,true,rs,refundAmt);//退款成功记录相关信息
			}else{
				orderPayManage.updateRedundOrderInfos(OriOrderNo,false,rs,refundAmt);//退款失败记录相关信息
			}
			%>
			退款操作后，业务处理结果  ：	[ 银联支付----手动退款 ] 处理完成 !!!!</br></br>
			<%
			logger.info("———————————————————————— [ 银联支付----手动退款 ] 处理完成————————————————————————————————————\n\n\n");
		} catch (Exception e) {
			logger.error(" [ 银联支付----手动分账 ] 发送 退款 请求成功后，进行相关操作失败：" + e.getMessage(),e);
			%>
			退款操作后，业务处理结果  ：	[ 银联支付----手动退款 ] 处理失败.......</br></br>
			<%
		}

	}



	/* 手动分账请求成功后  进行相关操作 */
	boolean orderSettlementStatus = false;
	if("0000".equals(r.get("respCode")) || "3023".equals(r.get("respCode"))){
		orderSettlementStatus = true;
	}
	try {
		logger.info(" [ 银联支付----手动分账 ] 向银联发送 分账 请求成功后，进行相关操作");
		BigDecimal orderAmt =  new BigDecimal(request.getParameter("OrderAmt").trim());
		orderPayManage.updateTakeConfirmOrderInfos(OriOrderNo, orderSettlementStatus,orderAmt);
		%>
		分账操作后，业务处理结果  ：	[ 银联支付----手动分账 ] 处理完成 !!!!</br></br>
		<%
		logger.info("———————————————————————— [ 银联支付----手动分账 ] 处理完成————————————————————————————————————");
	} catch (Exception e) {
	logger.error(" [ 银联支付----手动分账 ] 向银联发送 分账 请求成功后，进行相关操作失败：" + e.getMessage(),e);
	%>
	分账操作后，业务处理结果  ：	[ 银联支付----手动分账 ] 处理失败.......</br></br>
	<%
	}

}
%>

</form>
</body>

<form name="payConfirm" action="chinaPayDone.jsp" method="post">
支付类型：
<select name="fromWhere">
	<option value="<%= ChinaPayUtil.B2C %>">银联B2C支付</option>
	<option value="<%= ChinaPayUtil.NOCARD %>">银联无卡支付</option>
	<option value="<%= ChinaPayUtil.B2B %>">银联B2B支付</option>
	<option value="<%= ChinaPayUtil.MOBILE %>">银联手机支付</option>
</select>
</br>
分账序号(不能重复)：<input type="txt" size=50 name = 'num' value =''/></br>
支付流水号(pay_flow_id)：<input type="txt" size=50 name = 'OriOrderNo' value =''/></br>
订单支付日期：<input type="txt" size=44  name = 'OriTranDate' value =''/></br>
分账金额(单位：分)：<input type="txt"  size=50  name = 'OrderAmt' value =''/></br>
分账的详细信息：<input type="txt" size=50  name = 'MerSplitMsg' value =''/></br>
退款金额(单位：分)：<input type="txt" size=50  name = 'RefundAmt' value =''/></br>
退款的详细信息：<input type="txt" size=44  name = 'ReMerSplitMsg' value =''/></br>
操作密码：<input type="txt" size=50  name = 'passwords' value =''/></br>

<input type="submit" value="确认分账或退款">
</form>
</html>