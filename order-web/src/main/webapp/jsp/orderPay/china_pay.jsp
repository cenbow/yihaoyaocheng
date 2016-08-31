<%@ page import="com.yyw.yhyc.pay.chinapay.utils.PayUtil" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/29
  Time: 16:58
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>订单支付-提交到银联</title>
</head>
<body>

<br/>
<%
        String pay_url = PayUtil.getValue("pay_url");
%>
<form name="payment" action="<%= pay_url %>" method="POST">
    <c:choose>
        <c:when test="">
            <c:forEach items="${payRequestParamMap}" var="map">
                <input type="hidden" name = '${map.key}' id="${map.key}" value ='${map.value}'/>
            </c:forEach>
        </c:when>
    </c:choose>
</form>

<script type="text/javascript">
//    if(document.getElementById('MerSplitMsg').value=='0'){
//        alert("抱歉，该订单中的供应商存在问题，不能进行支付！");
//        window.close();
//    }else{
        document.payment.submit();
//    }
</script>
</body>
</html>
