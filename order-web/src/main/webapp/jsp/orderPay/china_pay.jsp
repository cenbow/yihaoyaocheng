<%@ page import="com.yyw.yhyc.pay.chinapay.utils.PayUtil" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/29
  Time: 16:58
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
</head>
<body>
<body>
flowIds: ${flowIds}
<br/>
<%
        String pay_url = PayUtil.getValue("pay_url");
%>
<form name="payment" action="<%= pay_url %>" method="POST">
    <%--<%--%>
        <%--Map<String, Object> sendMap=(Map)request.getAttribute("payRequestParamMap");--%>
        <%--for(Map.Entry<String, Object> entry:sendMap.entrySet()){--%>
    <%--%>--%>
    <%--<input type="hidden" name = '<%=entry.getKey() %>' id="<%=entry.getKey() %>" value ='<%=entry.getValue()%>'/>--%>
    <%--<%--%>
        <%--}--%>
    <%--%>--%>

</form>
<script language=JavaScript>
    //    if(document.getElementById('MerSplitMsg').value=='0'){
    //        alert("抱歉，该订单中的供应商存在问题，不能进行支付！");
    //        window.close();
    //    }else{
//    document.payment.submit();
    //    }
</script>
</body>
</html>
