<%@ page import="com.yyw.yhyc.order.enmu.SystemPayTypeEnum" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/4
  Time: 16:30
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>检查订单页-1号药城</title>
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
        <dl>
            <dt class="common-icon">3</dt>
            <dd>成功提交订单</dd>
        </dl>
    </div>
</div>
<div class="wapper get-order-info">
    <div class="goi-con">
        <h2>收货信息</h2>
        <ul>
            <c:choose>
                <c:when test="${dataMap != null && fn:length(dataMap.receiveAddressList) gt 0 }">
                    <c:forEach var="receiveAddress"  items="${dataMap.receiveAddressList}">
                        <li receiveAddressId="${receiveAddress.id}">
                            <p>${receiveAddress.receiverName}</p>
                            <p>${receiveAddress.provinceName} ${receiveAddress.cityName} ${receiveAddress.districtName} ${receiveAddress.address}</p>
                            <b class="inside-icon"></b>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <li>
                        <p>用户名</p>
                        <p>没有收获地址！</p>
                        <b class="inside-icon"></b>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
    <div class="goi-con-bill">
        <h2>发票信息</h2>
        <ul class="tc">
            <%--
                暂时不需要 增值税普通发票  ，以后需要再加
                <li billType="2">增值税普通发票<b class="inside-icon"></b></li>
            --%>
            <li billType="1" class="goi-selected">增值税专用发票<b class="inside-icon" style="display: inline"></b></li>
        </ul>
        <p class="goi-tips pt30">请选择发票种类，订单完成后配送商汇分别单独开具发票，如开票类型不一致，请单独提交结算。</p>
    </div>
    <h2 class="f14">商品信息</h2>


        <form id="createOrderForm" method="post" >
            <input type="hidden" name="custId" id="custId" value="${userDto.custId}"/>
            <input type="hidden" name="receiveAddressId" id="receiveAddressId" />
            <input type="hidden" name="billType" id="billType" value="1"/>

        <%--遍历每个供应商的信息  开始--%>
            <c:choose>
                <c:when test="${dataMap != null && fn:length(dataMap.allShoppingCart) gt 0 }">
                    <c:forEach var="shoppingCart"  items="${dataMap.allShoppingCart}" varStatus="shoppingCartVarStatus">
                        <div class="goi-product-list">
                            <div class="goi-table mt35">
                                <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].supplyId" id="${shoppingCart.seller.id}" value="${shoppingCart.seller.id}"/>
                                <input type="hidden" name="supplyId" value="${shoppingCart.seller.id}" supplyName="${shoppingCart.seller.enterpriseName}"/>
                                <table class="common-table f14">
                                    <tr>
                                        <th class="tl pl20">供应商：${shoppingCart.seller.enterpriseName}</th>
                                        <th>单价</th>
                                        <th>数量</th>
                                        <th>小计</th>
                                    </tr>
                                        <%--遍历该供应商的商品信息  开始--%>
                                        <c:choose>
                                            <c:when test="${shoppingCart != null && fn:length(shoppingCart.shoppingCartDtoList) gt 0 }">
                                                <c:forEach var="shoppingCartDto" items="${shoppingCart.shoppingCartDtoList}" varStatus="shoppingCartDtoVarStatus">
                                                    <input type="hidden" name="productId" value="${shoppingCartDto.productId}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].id" id="${shoppingCartDto.productId}" value="${shoppingCartDto.productId}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productCount" value="${shoppingCartDto.productCount}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productPrice" value="${shoppingCartDto.productPrice}"/>
                                                    <tr>
                                                        <td class="tl">
                                                            <img src="${STATIC_URL}/static/images/pro_img.jpg" class="fl pr20">
                                                            <h3><span class="ct-lable">渠道</span>${shoppingCartDto.productName} ${shoppingCartDto.specification}</h3>
                                                            <p class="f12">生产企业：${shoppingCartDto.manufactures}</p>
                                                        </td>
                                                        <td>¥ <fmt:formatNumber value="${shoppingCartDto.productPrice}" minFractionDigits="2"/></td>
                                                        <td>${shoppingCartDto.productCount}</td>

                                                        <td class="red">¥ <fmt:formatNumber value="${shoppingCartDto.productPrice * shoppingCartDto.productCount}" minFractionDigits="2"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                        </c:choose>
                                        <%--遍历该供应商的商品信息  结束--%>
                                </table>
                            </div>
                            <p class="inside-icon goi-mover-btn"><i class="inside-icon goi-arrow"></i>查看更多</p>

                            <div class="pt40 clearfix">
                                <div class="fl radio-select">
                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].payTypeId" id="${shoppingCart.seller.id}_payTypeId"/>
                                    选择支付方式：
                                    <label onclick="selectPayTypeId(${shoppingCart.seller.id},'<%=SystemPayTypeEnum.PayOnline.getPayType()%>')">
                                        <i class="inside-icon radio-skin"></i>在线支付
                                    </label>
                                    <label onclick="selectPayTypeId(${shoppingCart.seller.id},'<%=SystemPayTypeEnum.PayPeriodTerm.getPayType()%>')">
                                        <i class="inside-icon radio-skin"></i>账期支付
                                    </label>
                                    <label onclick="selectPayTypeId(${shoppingCart.seller.id},'<%=SystemPayTypeEnum.PayOffline.getPayType()%>')">
                                        <i class="inside-icon radio-skin"></i>
                                          <span class="pr"> 线下转账
                                            <i class="common-icon query-icon">
                                                <div class="tips-frame tc">
                                                    <i class="common-icon frame-icon"></i>
                                                    <p>
                                                      <a href="<%=request.getContextPath()%>/order/accountPayInfo/getByCustId/${userDto.custId}" target="_blank" class="blue">点击查看收款账户信息</a>
                                                    </p>
                                              </div>
                                            </i>
                                          </span>
                                    </label>
                                </div>
                                <div class="fr f14">订单金额：<span class="red">¥ <fmt:formatNumber value="${shoppingCart.productPriceCount}" minFractionDigits="2"/></span></div>
                            </div>
                            <p class="pt30">给卖家留言：<input type="text" class="goi-input" name="orderDtoList[${shoppingCartVarStatus.index}].leaveMessage" ></p>
                            <%--遍历每个供应商的信息  结束--%>
                        </div>
                    </c:forEach>
                    <div class="btn pt70 f16 tr">
                        总金额：<span class="red pr40">¥<fmt:formatNumber value="${dataMap.orderPriceCount}" minFractionDigits="2"/></span>
                        <a href="javascript:void(0)" id="createOrderButton" class="os-btn-pay tc" onclick="createOrder()">提交订单</a>
                    </div>
                </c:when>
                <c:otherwise>
                    您的购物车是空的!
                </c:otherwise>
            </c:choose>
            <input type="hidden" name="token" value="${token}" />
        </form>

</div>

<%@ include file="../footer.jsp"%>

<script type="text/javascript" src="${STATIC_URL}/static/js/order/createOrder.js"></script>
</body>
</html>