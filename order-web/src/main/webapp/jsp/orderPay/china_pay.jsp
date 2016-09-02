<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@page import="com.yyw.yhyc.pay.chinapay.utils.*"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>1号药城在线支付-跳转银联页面</title>
</head>
<body>
<%
    String pay_url = PayUtil.getValue("pay_url");
%>
<form name="payment" action="<%= pay_url %>" method="POST">
    <%
        Map<String, Object> sendMap = new HashMap<String, Object>();
        sendMap=(Map)request.getAttribute("PayMap");
        if(!sendMap.containsKey("MerSplitMsg")){
            sendMap.put("MerSplitMsg", "0");
        }
        System.out.println(sendMap);
        for(Map.Entry<String, Object> entry:sendMap.entrySet()){
            //request.setAttribute(entry.getKey(), entry.getValue());
            String params = "TranReserved;MerId;MerOrderNo;OrderAmt;CurryNo;TranDate;SplitMethod;BusiType;MerPageUrl;MerBgUrl;SplitType;MerSplitMsg;PayTimeOut;MerResv;Version;BankInstNo;CommodityMsg;Signature;AccessType;AcqCode;OrderExpiryTime;TranType;RemoteAddr;Referred;TranTime;TimeStamp;CardTranData";
            if(params.contains(entry.getKey())){
    %>
    <input type="hidden" name = '<%=entry.getKey() %>' id="<%=entry.getKey() %>" value ='<%=entry.getValue()%>'/>

    <%
            }
        }
    %>
</form>
<script language=JavaScript>
    if(document.getElementById('MerSplitMsg').value=='0'){
        alert("抱歉，该订单中的供应商存在问题，不能进行支付！");
        window.close();
    }else{
        document.payment.submit();
    }
</script>
</body>
</html>