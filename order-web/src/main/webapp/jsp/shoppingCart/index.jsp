<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/20
  Time: 7:01
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
    <title>进货单页-1号药城</title>
    <meta name="Keywords" content="">
    <meta name="description" content="">
    <%@ include file="../config.jsp"%>
    <link rel="stylesheet" href="${STATIC_URL}/static/css/inside.css"></head>
<body>

<%@ include file="../head.jsp"%>

<div class="wapper header">
    <h1 class="common-icon">
        <a href="#">1号药城 — 方便.快捷.第一</a>
    </h1>
    <div class="stepflex">
        <dl class="done">
            <dt class="common-icon">1</dt>
            <dd>我的进货单</dd>
        </dl>
        <dl>
            <dt class="common-icon">2</dt>
            <dd>确认订单信息</dd>
        </dl>
        <dl>
            <dt class="common-icon">3</dt>
            <dd>成功提交订单</dd>
        </dl>
    </div>
</div>
<div class="wapper shopping-cart">
    <div class="cart-table-th">
        <div class="wp">
            <div class="th th-chk">
                <div class="cart-checkbox">
                    <span class="inside-icon">全选所有商品</span>
                    全选
                </div>
            </div>
            <div class="th th-item">商品</div>
            <div class="th th-price">单价</div>
            <div class="th th-amount">数量</div>
            <div class="th th-sum">小计</div>
            <div class="th th-op">操作</div>
        </div>
    </div>

<%--遍历每个供应商的信息  开始--%>
    <c:choose>
        <c:when test="${allShoppingCart != null && fn:length(allShoppingCart) gt 0 }">
            <c:forEach var="shoppingCartListDto"  items="${allShoppingCart}" varStatus="shoppingCartListDtoVarStatus">
                <div class="order-holder">
                    <div class="holder-top">
                        <div class="cart-checkbox <c:if test='${shoppingCartListDto.needPrice == 0}'> select-all</c:if>" ><span class="inside-icon">全选所有商品</span></div>
                        <div class="mark-supplier">供应商：${shoppingCartListDto.seller.enterpriseName}</div>
                        <a class="lts-shop-icon f12" href="${domainPath}/front-web/shop/goShopHome?enterpriseId=${shoppingCartListDto.seller.enterpriseId}">进入店铺</a>
                        <p <c:if test="${shoppingCartListDto.needPrice == 0}"> style="display: none" </c:if> >
                            <input type="hidden" name="orderSamount" supplyId="${shoppingCartListDto.seller.enterpriseId}" supplyName="${shoppingCartListDto.seller.enterpriseName}"
                                   value="${shoppingCartListDto.seller.orderSamount}" buyPrice="${shoppingCartListDto.productPriceCount}" needPrice="${shoppingCartListDto.needPrice}">
                            此供应商订单起售价为<span class="from-price"><fmt:formatNumber value="${shoppingCartListDto.seller.orderSamount}" minFractionDigits="2"/></span>元，您目前已购买
                            <span class="buy-price"><fmt:formatNumber value="${shoppingCartListDto.productPriceCount}" minFractionDigits="2"/></span>元，还差
                            <span class="need-price"><fmt:formatNumber value="${shoppingCartListDto.needPrice}" minFractionDigits="2"/></span>元!
                        </p>
                    </div>

                    <%--遍历该供应商下的商品信息  开始--%>
                    <c:choose>
                        <c:when test="${shoppingCartListDto != null && fn:length(shoppingCartListDto.shoppingCartDtoList) gt 0}">
                            <c:forEach var="shoppingCartDto" items="${shoppingCartListDto.shoppingCartDtoList}"  varStatus="shoppingCartDtoVarStatus">
                                <div class="holder-list <c:if test="${!shoppingCartDto.existProductInventory}"> no-stock </c:if> ">
                                    <ul>
                                        <li class="fl td-chk">
                                            <c:if test="${shoppingCartDto.existProductInventory}">
                                                <div class="cart-checkbox <c:if test='${shoppingCartListDto.needPrice == 0}'> select-all </c:if><c:if test="${!shoppingCartDto.existProductInventory}"> checkbox-disable </c:if>"
                                                     shoppingCartId="${shoppingCartDto.shoppingCartId}" supplyId="${shoppingCartListDto.seller.enterpriseId}">
                                                    <span class="inside-icon" >全选所有商品</span>
                                                </div>
                                            </c:if>
                                        </li>
                                        <li class="fl td-pic" style="cursor: pointer" onclick="javascript:window.location.href='${domainPath}/front-web/product/productDetail/${shoppingCartDto.spuCode}/${shoppingCartDto.supplyId}'">
                                            <c:if test="${!shoppingCartDto.existProductInventory}">
                                                <span class="inside-icon">缺货</span>
                                            </c:if>
                                            <img class="productImageUrl" spuCode="${shoppingCartDto.spuCode}" src="${shoppingCartDto.productImageUrl}" alt="${shoppingCartDto.productName} ${shoppingCartDto.specification}">
                                        </li>
                                        <li class="fl td-item">
                                            <p class="item-title" style="cursor: pointer" onclick="javascript:window.location.href='${domainPath}/front-web/product/productDetail/${shoppingCartDto.spuCode}/${shoppingCartDto.supplyId}'">
                                                ${shoppingCartDto.productName} ${shoppingCartDto.specification}
                                            </p>`
                                            <p>${shoppingCartDto.manufactures}</p>
                                        </li>
                                        <li class="fl td-price">
                                            ¥<span><fmt:formatNumber value="${shoppingCartDto.productPrice}" minFractionDigits="2"/></span>
                                        </li>
                                        <li class="fl td-amount">
                                            <div class="it-sort-col5 clearfix pr" style="width: 120px;">
                                                <div class="clearfix" style="padding-left: 20px;">
                                                    <div class="its-choose-amount fl">
                                                        <div class="its-input">
                                                            <a href="javascript:;" class="its-btn-reduce">-</a>
                                                            <a href="javascript:;" class="its-btn-add">+</a>
                                                            <input value="${shoppingCartDto.productCount}" class="its-buy-num" shoppingCartId="${shoppingCartDto.shoppingCartId}"
                                                                   saleStart="${shoppingCartDto.saleStart}" upStep = "${shoppingCartDto.upStep}" preValue="${shoppingCartDto.productCount}">
                                                        </div>
                                                    </div>
                                                    <%--<div class="pt13 pl20 fl">瓶</div>--%>
                                                </div>
                                                <%--<c:if test="${shoppingCartDto.saleStart > 0 }">--%>
                                                    <%--<span class="color-gray9">${shoppingCartDto.saleStart}起售</span>--%>
                                                <%--</c:if>--%>
                                                <c:if test="${shoppingCartDto.upStep > 0 }">
                                                    <%--<div class="pr-list-tips-frame tc">
                                                        <i class="common-icon pl-frame-icon"></i><p>最小拆零包装：${shoppingCartDto.upStep}</p>
                                                    </div>--%>
                                                    <%--<span class="color-gray9">${shoppingCartDto.upStep}递增</span>--%>
                                                    <span class="color-gray9">最小拆零包装:${shoppingCartDto.upStep}${shoppingCartDto.unit}</span>
                                                </c:if>
                                            </div>
                                        </li>
                                        <li class="fl td-sum">
                                            <input type="hidden" name="productSettlementPrice" value="${shoppingCartDto.productSettlementPrice}">
                                            ¥<span><fmt:formatNumber value="${shoppingCartDto.productSettlementPrice}" minFractionDigits="2"/></span>
                                        </li>
                                        <li class="fl td-op"><a href="javascript:deleteShoppingCart(${shoppingCartDto.shoppingCartId});" class="btn-delete">删除</a></li>
                                    </ul>
                                </div>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                    <%--遍历该供应商下的商品信息  结束--%>

                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            您的购物车是空的!
        </c:otherwise>
    </c:choose>
<%--遍历每个供应商的信息  结束--%>

    <div>
        <form id="submitCheckOrderPage" method="post">
        </form>
    </div>


    <div class="order-bottom">
        <div class="operation">
            <div class="th-chk">
                <div class="cart-checkbox">
                    <span class="inside-icon">全选所有商品</span>
                    全选
                </div>
            </div>
            <a class="delete-items">删除选中商品</a>
            <a class="clear-items">清除失效商品</a>
        </div>
        <div class="oper-info">
            <div class="total-price">
                商品总额： <em>¥</em>
                <span>0.00</span>
                <%--<input type="hidden" id="productAllPrice">--%>
            </div>
            <div class="total-item">
                品种总计：
                <span>0</span>
            </div>
        </div>
        <div class="btn">
            <a class="os-btn-pay tc" href="javascript:void(0);">立即结算</a>
            <a class="os-btn-order tc" href="index.html">继续采购</a>
        </div>
    </div>

</div>


<%@ include file="../footer.jsp"%>

<script src="${STATIC_URL}/static/js/shoppingCart/common.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/dialog.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/shoppingCart/shoppingCart.js"></script>
</body>
</html>


