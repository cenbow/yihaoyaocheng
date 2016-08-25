<%@ page import="com.yyw.yhyc.order.enmu.SystemPayTypeEnum" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/4
  Time: 16:48
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>订单提交成功页-1号药城</title>
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
    <div class="order-content">
        <p class="tc f18 pt40"><i class="common-icon os-icon"></i>订单提交成功！</p>
        <p class="tc os-text pt30 ">线下转账订单请进入订单中心获取收款账户信息后转账</p>

        <c:choose>
            <c:when test="${onLinePayOrderPriceCount > 0}">
                <p class="f18 red fb tc mt30">在线支付总金额： ¥ <fmt:formatNumber value="${onLinePayOrderPriceCount}" minFractionDigits="2"/></p>
                <div class="pay-type mt40">
                    <p class="pay-type-top">请选择在线支付方式：</p>
                    <div class="radio-select">
                        <label>
                            <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-cmb"></i><br><span>
                            <i class="common-icon radio-tip"></i>需开通招商银行企业网银</span>
                        </label>
                        <label>
                            <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-pay"></i><br><span>银联卡支付</span>
                        </label>
                        <label>
                            <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-pay"></i><br><span class="pr">网银支付</span>
                        </label>
                    </div>
                </div>
                <div class="mt45 tc btn">
                    <a href="javascript:alert('开发中,敬请期待!');" class="os-btn-pay">立即在线支付</a>
                    <a href="<%=request.getContextPath()%>/order/buyerOrderManage" class="os-btn-order">订单中心</a>
                </div>
            </c:when>

            <c:otherwise>
                <div class="mt45 tc btn">
                    <a href="<%=request.getContextPath()%>/order/buyerOrderManage" class="os-btn-order">订单中心</a>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="os-tips mt40 pt50">
            <p>您的订单已根据不同供应商及不同账期商品拆分为多单，详情请进入订单中心查看。</p>
            <p>在线支付及线下转账的订单请尽快付款，提交订单后24小时内未完成支付，订单将自动取消！</p>
        </div>

        <table class="common-table mt20">
            <tr>
                <th>订单号</th>
                <th>供应商</th>
                <th>订单金额</th>
                <th>支付方式</th>
            </tr>
            <c:set var="offlinePayType" value="<%=SystemPayTypeEnum.PayOffline.getPayType()%>"/>
            <c:choose>
                <c:when test="${orderDtoList != null && fn:length(orderDtoList) gt 0 }">
                    <c:forEach var="orderDto"  items="${orderDtoList}">

                        <c:choose>
                            <c:when test="${orderDto.payTypeId == offlinePayType}">
                                <tr>
                                    <td>${orderDto.flowId}</td>
                                    <td>${orderDto.supplyName}</td>
                                    <td>¥ ${orderDto.orderTotal}</td>
                                    <td>
                                            <span class="pr">${orderDto.payTypeName}
                                              <i class="common-icon query-icon">
                                                <div class="tips-frame">
                                                  <i class="common-icon frame-icon"></i>
                                                  <p>
                                                      <a href="<%=request.getContextPath()%>/order/accountPayInfo/getByCustId/${orderDto.supplyId}" target="_blank" class="blue">点击查看收款账户信息</a>
                                                  </p>
                                                </div>
                                              </i>
                                            </span>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td>${orderDto.flowId}</td>
                                    <td>${orderDto.supplyName}</td>
                                    <td>¥ <fmt:formatNumber value="${orderDto.orderTotal}" minFractionDigits="2"/></td>
                                    <td>${orderDto.payTypeName}</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4">无订单数据!</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>
    </div>
</div>

<%@ include file="../footer.jsp"%>

<script type="text/javascript" src="${STATIC_URL}/static/js/dialog.js"></script>
</body>
</html>