<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*,java.text.SimpleDateFormat"%>
<%@page import="chinapay.*"%>
<%@page import="com.yyw.yhyc.pay.chinapay.httpClient.*"%>
<%@page import="com.yyw.yhyc.pay.chinapay.pay.*"%>
<%@page import="com.yyw.yhyc.pay.chinapay.utils.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<br>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>1号药城-分账退款</title>
</head>
<body>
<form name="payment"  method="POST" target="_blank">

<%

//暂时用这两个分账号作调试   999901511180003  999901511180004
String passwords=request.getParameter("passwords");

if(passwords!=null&&!"".equals(passwords)&&"fkyqazsa2016".equals(passwords)){

Map<String, Object> sendMap = new HashMap<String, Object>();
String OriOrderNo=request.getParameter("OriOrderNo");
SimpleDateFormat datefomet=new SimpleDateFormat("yyyyMMdd,HHmmss");
SimpleDateFormat fomet=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
Date date=new Date();
String fDate=datefomet.format(date);

if(OriOrderNo!=null&&!"".equals(OriOrderNo)){

sendMap.put("MerOrderNo", request.getParameter("OriOrderNo").trim()+"FZ"+request.getParameter("num"));//确认收货定单号 需要传输，订单号规则     原父定单号+01
sendMap.put("TranDate",fDate.split(",")[0]);
sendMap.put("TranTime", fDate.split(",")[1]);
sendMap.put("OriTranDate", request.getParameter("OriTranDate").trim());//原定单交易日期 需要传输
sendMap.put("OrderAmt", request.getParameter("OrderAmt").trim());//定单金额，需要转过来
sendMap.put("OriOrderNo", request.getParameter("OriOrderNo").trim());//原定单号 需要传输
sendMap.put("fromWhere", request.getParameter("fromWhere").trim());
// 返回参数请参考 (新一代商户接入手册V2.1-) 后续类交易接口 的异步返回报文章
sendMap.put("MerBgUrl", "http://www.fangkuaiyi.com/ConfirmCallBack.action");//不需要转过来
sendMap.put("MerSplitMsg", request.getParameter("MerSplitMsg").trim());//分账信息，需要传输过来

Map<String, String> r=new ChinaPay().sendPay2ChinaPay(sendMap);//取消支付的定单接口方法


for(Map.Entry<String, String> entry:r.entrySet()){
	//request.setAttribute(entry.getKey(), entry.getValue());
	String params = "TranReserved;MerId;MerOrderNo;OrderAmt;CurryNo;TranDate;SplitMethod;BusiType;MerPageUrl;MerBgUrl;SplitType;MerSplitMsg;PayTimeOut;MerResv;Version;BankInstNo;CommodityMsg;Signature;AccessType;AcqCode;OrderExpiryTime;TranType;RemoteAddr;Referred;TranTime;TimeStamp;CardTranData:fromWhere";
	//if(params.contains(entry.getKey())){
%>	
	分账操作操作结果 ：<input type="txt" name = '<%=entry.getKey() %>' value ='<%=entry.getValue()%>'/></br>
<%	
	
}
	
  if(!request.getParameter("ReMerSplitMsg").equals("")&&!request.getParameter("RefundAmt").equals("")){
	sendMap = new HashMap<String, Object>();
	sendMap.put("OriOrderNo", request.getParameter("OriOrderNo").trim());//原定单号 需要传输
	sendMap.put("OriTranDate",request.getParameter("OriTranDate").trim());//原定单交易日期 需要传输
	sendMap.put("TranDate",fDate.split(",")[0]);
	sendMap.put("TranTime", fDate.split(",")[1]);
	sendMap.put("MerOrderNo",request.getParameter("OriOrderNo").trim()+"TK");//分账信息，需要传输过来
	sendMap.put("MerSplitMsg",request.getParameter("ReMerSplitMsg").trim());//分账信息，需要传输过来
	sendMap.put("RefundAmt",request.getParameter("RefundAmt").trim());//分账信息，需要传输过来
	sendMap.put("fromWhere", request.getParameter("fromWhere").trim());
	sendMap.put("MerBgUrl", "http://www.fangkuaiyi.com/RedundCallBack.action");//要转过来,成功后会返回应答
	Map<String, String> rs=new ChinaPay().cancelOrder(sendMap);
	for(Map.Entry<String, String> entry:rs.entrySet()){
		System.out.println("退款操作结果："+entry.getKey()+"=="+entry.getValue());
	}
   }
	
 }
}else{
	System.out.println("提示：请输入 正确的操作密码。。。。。。。。。。");
}
%>

</form>
<script language=JavaScript>
	
</script>	
</body>
</br></br></br>
<form name="payConfirm" action="chinaPayDone.jsp" method="post">
支付类型：
<select name="fromWhere">
	<option value ="1">b2c</option>
	<option value ="2">手机</option>
	<option value="3">企业</option>
</select>
</br>
分账序号：<input type="txt" size=50 name = 'num' value =''/></br>
原订单号：<input type="txt" size=50 name = 'OriOrderNo' value =''/></br>
订单支付日期：<input type="txt" size=44  name = 'OriTranDate' value =''/></br>
实付金额：<input type="txt"  size=50  name = 'OrderAmt' value =''/></br>
分账信息：<input type="txt" size=50  name = 'MerSplitMsg' value =''/></br>
退款金额：<input type="txt" size=50  name = 'RefundAmt' value =''/></br>
退款分账信息：<input type="txt" size=44  name = 'ReMerSplitMsg' value =''/></br>
操作密码：<input type="txt" size=50  name = 'passwords' value =''/></br>

<input type="submit" value="确认分账及退款">
</form>
</html>