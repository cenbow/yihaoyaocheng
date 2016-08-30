<%@ page import="com.yyw.yhyc.pay.chinapay.utils.PayUtil" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/29
  Time: 11:09
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset= utf-8"/>
    <meta http-equiv="Expires" content="1/1/1990"/>
    <meta http-equiv="Pragma"  content="no-cache, must-revalidate"/>
    <title>订单支付-提交到招商银行</title>
</head>
<body>
招商银行支付测试：
<br/>

<form name="payment" action="${payRequestParamMap.CMB_PAY_URL}" method="POST">
    <TEXTAREA id="order" name="order" rows="4" cols="110">${payRequestParamMap.order}</TEXTAREA>
    <br><BR>签名信息：<BR>
    <TEXTAREA id="sigdat" name="sigdat" rows="8" cols="110">${payRequestParamMap.sigdat}</TEXTAREA>
    <BR><BR>
    <input type="hidden" name="charset"  value="utf-8">
    <input type="submit" value="立即支付">
</form>

<script language=JavaScript>
//    if(document.getElementById('MerSplitMsg').value=='0'){
//        alert("抱歉，该订单中的供应商存在问题，不能进行支付！");
//        window.close();
//    }else{
//        document.payment.submit();
//    }
</script>
</body>
</html>
