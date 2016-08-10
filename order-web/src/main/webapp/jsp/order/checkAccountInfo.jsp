<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/4
  Time: 17:12
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>查看收款账号信息-1号药城</title>
    <meta name="Keywords" content="">
    <meta name="description" content="">
    <%@ include file="../config.jsp"%>
    <link rel="stylesheet" href="${STATIC_URL}/static/css/inside.css">
</head>
<body>

<%@ include file="../head.jsp"%>

<div class="wapper header">
    <h1 class="common-icon"><a href="#">1号药城 — 方便.快捷.第一</a></h1>
</div>
<div class="wapper check-info">
    <p class="pt35 f14">请于24小时内完成汇款，汇款账户如下：</p>
    <div class="mt45 f14 ci-hank-info">
        <p>户名：${dataMap.receiveAccountName}</p>
        <p>账户：${dataMap.receiveAccountNo}</p>
        <p>开户行：${dataMap.subbankName}</p>
    </div>
    <div class="ci-tips pt45">
        <p>线下转账说明：</p>
        <p>1.  汇款时请<span class="red">备注订单号</span>，用于保证订单及时核销。此订单号务必填写正确，请勿增加其他文字说明；</p>
        <p>2.  订单号需填写在电汇凭证的【汇款用途】、【附言】、【摘要】等栏内（因不同银行备注不同，最好在所有可填写备注的地方均填写）；</p>
        <p>3.  请按照订单实际金额转账，请勿多转账或少转账</p>
    </div>
</div>

<%@ include file="../footer.jsp"%>

</body>
</html>
