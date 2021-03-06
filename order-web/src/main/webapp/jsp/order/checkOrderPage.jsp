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
    <h1 class="common-icon"><a href="${mallDomain}">1号药城 — 方便.快捷.第一</a></h1>
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
                    <c:forEach var="receiveAddress"  items="${dataMap.receiveAddressList}" varStatus="receiveAddressVarStatus">

                        <c:choose>
                            <c:when test="${receiveAddressVarStatus.index == 0}">
                                <li receiveAddressId="${receiveAddress.id}" class="goi-selected">
                                    <p>${receiveAddress.receiverName}</p>
                                    <p>${receiveAddress.provinceName} ${receiveAddress.cityName} ${receiveAddress.districtName} ${receiveAddress.address}</p>
                                    <b class="inside-icon" style="display: block;"></b>
                                    <input type="hidden" id="defaultReceiveAddressId" value="${receiveAddress.id}">
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li receiveAddressId="${receiveAddress.id}">
                                    <p>${receiveAddress.receiverName}</p>
                                    <p>${receiveAddress.provinceName} ${receiveAddress.cityName} ${receiveAddress.districtName} ${receiveAddress.address}</p>
                                    <b class="inside-icon"></b>
                                </li>
                            </c:otherwise>
                        </c:choose>

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
            <li billType="2" class="goi-selected">增值税普通发票<b class="inside-icon" style="display: inline"></b></li>
            <li billType="1">增值税专用发票<b class="inside-icon"></b></li>
        </ul>
        <p class="goi-tips pt30">请选择发票种类，订单完成后配送商汇分别单独开具发票，如开票类型不一致，请单独提交结算。</p>
    </div>
    <h2 class="f14">商品信息</h2>


        <form id="createOrderForm" method="post" >
            <input type="hidden" name="custId" id="custId" value="${userDto.custId}"/>
            <input type="hidden" name="receiveAddressId" id="receiveAddressId" />
            <input type="hidden" name="billType" id="billType" value="2"/>
            <input type="hidden" name="allOrderShareMoney" id="allOrderShareMoney" value="${dataMap.allOrderShareMoney}"/>

        <%--遍历每个供应商的信息  开始--%>
            <c:choose>
                <c:when test="${dataMap != null && fn:length(dataMap.allShoppingCart) gt 0 }">
                    <c:forEach var="shoppingCart"  items="${dataMap.allShoppingCart}" varStatus="shoppingCartVarStatus">
                        <div class="goi-product-list">
                            <div class="goi-table mt35">
                                <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].supplyId" id="${shoppingCart.seller.enterpriseId}" value="${shoppingCart.seller.enterpriseId}"/>
                                <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].paymentTerm" value="${shoppingCart.paymentTermCus}"/>
                                <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].accountAmount" value="${shoppingCart.accountAmount}"/>
                                <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].orderFullReductionMoney" value="${shoppingCart.orderFullReductionMoney}"/>
                                <input type="hidden" name="supplyId" value="${shoppingCart.seller.enterpriseId}" supplyName="${shoppingCart.seller.enterpriseName}"/>
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
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].shoppingCartId" value="${shoppingCartDto.shoppingCartId}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].id" id="${shoppingCartDto.productId}" value="${shoppingCartDto.productId}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].spuCode" value="${shoppingCartDto.spuCode}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productCodeCompany" value="${shoppingCartDto.productCodeCompany}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productCount" value="${shoppingCartDto.productCount}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productPrice" value="${shoppingCartDto.productPrice}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productName" value="${shoppingCartDto.productName}"/>
                                                    
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].periodProduct" value="${shoppingCartDto.periodProduct}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].paymentTerm" value="${shoppingCartDto.paymentTerm}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].manufactures" value="${shoppingCartDto.manufactures}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].fromWhere" value="${shoppingCartDto.fromWhere}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].promotionId" value="${shoppingCartDto.promotionId}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].promotionCollectionId" value="${shoppingCartDto.promotionCollectionId}"/>
                                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].promotionName" value="${shoppingCartDto.promotionName}"/>
                                                    <tr>
                                                        <td class="tl" style="cursor: pointer" onclick="javascript:window.location.href='${mallDomain}/product/productDetail/${shoppingCartDto.spuCode}/${shoppingCartDto.supplyId}'">
                                                            <img spuCode="${shoppingCartDto.spuCode}" class="fl pr20 productImageUrl" onerror="this.src='http://oms.yaoex.com/static/images/product_default_img.jpg'">
                                                            <h3>
                                                                <c:if test="${shoppingCartDto.isChannel == 1}">
                                                                    <span class="ct-lable">渠道</span>
                                                                </c:if>
                                                                ${shoppingCartDto.productName} ${shoppingCartDto.specification}
                                                            </h3>
                                                            <p class="f12">生产企业：${shoppingCartDto.manufactures}</p>
                                                        </td>
                                                        <td>
                                                            <c:if test="${shoppingCartDto.promotionId != null && shoppingCartDto.promotionId > 0 }">
                                                                <p style="padding-bottom: 12px">
                                                                    <span style="color: #fff;background: #fe5050;padding: 4px 10px;">限时特价</span>
                                                                </p>
                                                            </c:if>
                                                            <p>¥ <fmt:formatNumber value="${shoppingCartDto.productPrice}" minFractionDigits="2"/></p>
                                                        </td>
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
                                    <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].payTypeId" class="payTypeId" supplyName="${shoppingCart.seller.enterpriseName}"/>
                                    选择支付方式：
                                    <label>
                                        <i class="inside-icon radio-skin" supplyId="${shoppingCart.seller.enterpriseId}" payTypeId="<%=SystemPayTypeEnum.PayOnline.getPayType()%>"></i>在线支付
                                    </label>

                                    <%--账期支付按钮能勾选的前提条件：既要 资信有额度 又要设置客户账期  --%>
                                    <c:choose>
                                        <c:when test="${shoppingCart.accountAmount == 1 && shoppingCart.paymentTermCus > 0}">
                                            <label>
                                                <i class="inside-icon radio-skin" supplyId="${shoppingCart.seller.enterpriseId}" payTypeId="<%=SystemPayTypeEnum.PayPeriodTerm.getPayType()%>"></i>账期支付
                                            </label>
                                        </c:when>
                                        <c:otherwise>
                                            <label class="label-disabled">
                                                <i class="inside-icon radio-skin"></i>
                                                <span class="pr">账期支付
                                                  <i class="common-icon query-icon">
                                                    <div class="tips-frame tc">
                                                      <i class="common-icon frame-icon"></i>
                                                      <p>该订单无额度可用！</p>
                                                    </div>
                                                  </i>
                                                </span>
                                            </label>
                                        </c:otherwise>
                                    </c:choose>


                                    <label>
                                        <i class="inside-icon radio-skin" supplyId="${shoppingCart.seller.enterpriseId}" payTypeId="<%=SystemPayTypeEnum.PayOffline.getPayType()%>" ></i>
                                          <span class="pr"> 线下转账
                                            <i class="common-icon query-icon">
                                                <div class="tips-frame tc">
                                                    <i class="common-icon frame-icon"></i>
                                                    <p>
                                                      <a href="<%=request.getContextPath()%>/order/accountPayInfo/getByCustId/${shoppingCart.seller.enterpriseId}" target="_blank" class="blue">点击查看收款账户信息</a>
                                                    </p>
                                              </div>
                                            </i>
                                          </span>
                                    </label>
                                </div>
                            </div>
                              <div class="fr">
                                <div class="f14">订单金额：<span class="red">¥ <fmt:formatNumber value="${shoppingCart.productPriceCount}" minFractionDigits="2"/></span></div>
                                <c:if test="${shoppingCart.orderFullReductionMoney != null && shoppingCart.orderFullReductionMoney > 0 }">
                                   <div class="f14">满立减金额：<span class="red">¥ -<fmt:formatNumber value="${shoppingCart.orderFullReductionMoney}" minFractionDigits="2"/></span></div>                 
                                </c:if>
                              </div>
                            <p class="pt30">
						                            给卖家留言：<input type="text" class="goi-input" name="orderDtoList[${shoppingCartVarStatus.index}].leaveMessage" style="margin-right: 100px;">
						       
						         <c:choose>
					                <c:when test="${shoppingCart.adviserList != null && fn:length(shoppingCart.adviserList) gt 0 }">
					                 	  销售顾问：<select  class="goi-input"  id="adviserName" name="orderDtoList[${shoppingCartVarStatus.index}].adviserName">
					                 	 <option></option> 
					                    <c:forEach var="adviserInfo"  items="${shoppingCart.adviserList}">
					                        <option name="orderDtoList[${shoppingCartVarStatus.index}].adviserName" value="${adviserInfo.adviserCode};${adviserInfo.adviserName};${adviserInfo.adviserPhoneNumber};${adviserInfo.adviserRemark}">${adviserInfo.adviserName}</option>
					                    </c:forEach>
					                    </select>
                              		   	<input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].adviserPhoneNumber" value=0 />
					                </c:when>
					            </c:choose>
                            </p>
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
<script type="text/javascript" src="${STATIC_URL}/static/js/dialog.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/order/createOrder.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/inside.js"></script>
</body>
</html>