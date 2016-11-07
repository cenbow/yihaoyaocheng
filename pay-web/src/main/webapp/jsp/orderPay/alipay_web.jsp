<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/10/29
  Time: 15:32
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset= utf-8"/>
    <meta http-equiv="Expires" content="1/1/1990"/>
    <meta http-equiv="Pragma"  content="no-cache, must-revalidate"/>
    <title>订单支付-提交到支付宝</title>
    <script type="text/javascript">
        //    if(document.getElementById('MerSplitMsg').value=='0'){
        //        alert("抱歉，该订单中的供应商存在问题，不能进行支付！");
        //        window.close();
        //    }else{
        //        document.payment.submit();
        //    }
        window.location.href = "${payRequestParamMap.payRequestUrl}";
    </script>
</head>
<body>
支付宝跳转中...
<br/>

<%--<form name="payment" action="${payRequestParamMap.payRequestUrl}" method="get">--%>
    <%--&lt;%&ndash;<TEXTAREA id="order" name="order" rows="4" cols="110">${payRequestParamMap.order}</TEXTAREA>&ndash;%&gt;--%>
    <%--&lt;%&ndash;<br><BR>签名信息：<BR>&ndash;%&gt;--%>
    <%--&lt;%&ndash;<TEXTAREA id="sigdat" name="sigdat" rows="8" cols="110">${payRequestParamMap.sigdat}</TEXTAREA>&ndash;%&gt;--%>
    <%--&lt;%&ndash;<BR><BR>&ndash;%&gt;--%>
    <%--<input type="hidden" name="charset"  value="utf-8">--%>
    <%--<input type="submit" id="" value="立即支付">--%>
<%--</form>--%>


</body>
</html>
