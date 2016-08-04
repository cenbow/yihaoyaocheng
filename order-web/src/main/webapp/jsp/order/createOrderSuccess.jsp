<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/4
  Time: 16:48
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>订单提交成功-1号药城</title>
    <meta name="Keywords" content="">
    <meta name="description" content="">
    <%@ include file="../config.jsp"%>
    <link rel="stylesheet" href="${STATIC_URL}/static/css/inside.css">
</head>
<body>

<%@ include file="../head.jsp"%>
<div class="wapper header">
    <h1 class="common-icon"><a href="#">1号药城 — 方便.快捷.第一</a></h1>
    <div class="stepflex">
        <dl class="done">
            <dt class="common-icon">1</dt>
            <dd>我的进货单</dd>
        </dl>
        <dl class="done">
            <dt class="common-icon">2</dt>
            <dd>确认订单信息</dd>
        </dl>
        <dl class="done">
            <dt class="common-icon">3</dt>
            <dd>成功提交订单</dd>
        </dl>
    </div>
</div>

<div class="wapper order-sucess">
    <div class="os-content">
        <p class="tc f18 pt40"><i class="inside-icon os-icon"></i>订单提交成功！</p>
        <p class="tc red pt30">线下转账订单请进入订单中心获取收款账户信息后转账</p>
        <p class="pt50">您的订单已根据不同供应商及不同账期商品拆分为多单，详情请进入订单中心查看。</p>
        <p>在线支付及线下转账的订单请尽快付款，提交订单后24小时内未完成支付，订单将自动取消！</p>
        <table class="common-table mt20">
            <tr>
                <th>订单号</th>
                <th>供应商</th>
                <th>订单金额</th>
                <th>支付方式</th>
            </tr>
            <tr>
                <td>ZXD2016071914230100000101</td>
                <td>上海九州通医药有限公司</td>
                <td>¥ 10000.00</td>
                <td>账期支付</td>
            </tr>
            <tr>
                <td>ZXD2016071914230100000101</td>
                <td>上海九州通医药有限公司</td>
                <td>¥ 900.00</td>
                <td>账期支付</td>
            </tr>
            <tr>
                <td>ZXD2016071914230100000101</td>
                <td>上海九州通医药有限公司</td>
                <td>¥ 2000.00</td>
                <td>
            <span class="pr">
              线下转账
              <i class="common-icon query-icon">
                <div class="tips-frame">
                  <i class="common-icon frame-icon"></i>
                  <p>
                  <a href="<%=request.getContextPath()%>/order/checkAccountInfo" target="_blank" class="blue">点击查看收款账户信息</a>
                  </p>
                </div>
              </i>
            </span>
                </td>
            </tr>
            <tr>
                <td>ZXD2016071914230100000101</td>
                <td>上海九州通医药有限公司</td>
                <td>¥ 40000.00</td>
                <td>在线支付</td>
            </tr>
            <tr>
                <td>ZXD2016071914230100000101</td>
                <td>上海九州通医药有限公司</td>
                <td>¥ 5000.00</td>
                <td>在线支付</td>
            </tr>
        </table>
        <p class="f18 red fb tc mt40">在线支付总金额： ¥ 45000.00</p>
        <div class="mt45 tc btn">
            <a href="#" class="os-btn-pay">立即在线支付</a>
            <a href="#" class="os-btn-order">订单中心</a>
        </div>
    </div>
</div>


<%@ include file="../footer.jsp"%>

</body>
</html>