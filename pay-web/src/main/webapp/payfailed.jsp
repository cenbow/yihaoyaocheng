<%--
  Created by IntelliJ IDEA.
  User: bigwing
  Date: 2016/10/26
  Time: 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <head>
        <meta charset="utf-8">
        <%@ include file="jsp/config.jsp"%>
        <title>在线支付异常</title>
        <meta name="Keywords" content="">
        <meta name="description" content="">
        <link rel="stylesheet" href="static/css/store.css">
        <link rel="stylesheet" href="static/css/swiper.css">
        <link rel="stylesheet" href="static/css/fancybox.css">
        <style>
            .pay_error{width: 500px;margin: 0 auto;text-align: center;}
            button{width:168px;height: 48px;font-size: 18px;color: #fff;background: #fe5050;border: none;cursor: pointer;}
            .text{padding: 72px 0 48px 0;font-size: 16px;color: #333;}
        </style>
    </head>
<body>

<%@ include file="jsp/head.jsp"%>




<div class="wapper store-page clearfix">
    <div class="pay_error">
        <p><img src="static/images/pay_error.jpg" /></p>
        <p class="text">支付服务异常，请您稍后重试！</p>
        <p><button>返回</button></p>
    </div>
</div>

<%@ include file="jsp/footer.jsp"%>


</body>
</html>
