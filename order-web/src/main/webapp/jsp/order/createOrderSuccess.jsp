<%@ page import="com.yyw.yhyc.order.enmu.SystemPayTypeEnum" %>
<%@ page import="com.yyw.yhyc.order.enmu.OnlinePayTypeEnum" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/4
  Time: 16:48
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
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
        <dl class="done">
            <dt class="common-icon">3</dt>
            <dd>成功提交订单</dd>
        </dl>
    </div>
</div>
<div class="wapper order-sucess">
    <div class="order-content">
        <p class="tc f18 pt40"><i class="common-icon os-icon"></i>订单提交成功！</p>

        <c:choose>
            <c:when test="${orderDtoList != null && fn:length(orderDtoList) gt 1 }">
                <div class="os-tips mt40 pt50">
                    <p style="color:#666666;font-size:14px">
                        您的订单已根据不同供应商及不同账期商品拆分为多单，在线支付的订单请进入订单中心分别支付，<br/>
                        线下转账订单请进入订单中心获取收款账户信息后转账。
                    </p>
                    <p style="padding-top: 30px">
                        在线支付及线下转账的订单请尽快付款，提交订单后24小时内未完成支付，订单将自动取消！
                    </p>
                </div>
                <div class="mt45 tc btn" style="padding-bottom: 50px;  margin-bottom: 50px; border-bottom: 1px dashed #ededed;">
                    <a href="<%=request.getContextPath()%>/order/buyerOrderManage" class="os-btn-pay">订单中心</a>
                </div>
            </c:when>
            <c:otherwise>
                <p class="tc os-text pt30 ">线下转账订单请进入订单中心获取收款账户信息后转账</p>
                <c:choose>
                    <c:when test="${onLinePayOrderPriceCount > 0}">
                        <p class="f18 red fb tc mt30">在线支付金额： ¥ <fmt:formatNumber value="${onLinePayOrderPriceCount}" minFractionDigits="2"/></p>
                        <div class="pay-type mt40">
                            <p class="pay-type-top">请选择在线支付方式：</p>
                            <div class="radio-select">
                                <%--<label payTypeId="<%=OnlinePayTypeEnum.MerchantBank.getPayTypeId()%>">--%>
                                    <%--<i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-cmb"></i><br><span>--%>
                                <%--<i class="common-icon radio-tip"></i>需开通招商银行企业网银</span>--%>
                                <%--</label>--%>
                                    <%--<label payTypeId="<%=OnlinePayTypeEnum.CmbEPlus.getPayTypeId()%>">--%>
                                        <%--<i class="inside-icon radio-skin"></i><img src="${STATIC_URL}/static/images/cmb_eplus_pay.jpg" alt=""><br><span class="pr"></span>--%>
                                    <%--</label>--%>
                                    <label payTypeId="<%=OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId()%>">
                                        <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-pay"></i><br><span>银联卡支付</span>
                                    </label>
                                    <label payTypeId="<%=OnlinePayTypeEnum.UnionPayB2C.getPayTypeId()%>">
                                        <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-pay"></i><br><span class="pr">非企业网银支付</span>
                                    </label>
                                    <label payTypeId="<%=OnlinePayTypeEnum.UnionPayB2B.getPayTypeId()%>">
                                        <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-pay"></i><br><span class="pr">企业网银支付</span>
                                    </label>
                                    <label payTypeId="<%=OnlinePayTypeEnum.AlipayWeb.getPayTypeId()%>">
                                        <i class="inside-icon radio-skin"></i><img src="${STATIC_URL}/static/images/alipay.png" alt=""><br><span class="pr"></span>
                                    </label>
                                <input type="hidden" id="payTypeId">
                            </div>
                        </div>
                        <div class="mt45 tc btn">
                            <a href="javascript:pay();" class="os-btn-pay">立即在线支付</a>
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
            </c:otherwise>
        </c:choose>

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
                        <tr>
                            <td>${orderDto.flowId}<input type="hidden" name="flowId" value="${orderDto.flowId}"></td>
                            <td>${orderDto.supplyName}</td>
                            <td>¥ <fmt:formatNumber value="${orderDto.orderTotal}" minFractionDigits="2"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${orderDto.payTypeId == offlinePayType}">
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
                                    </c:when>
                                    <c:otherwise>
                                        ${orderDto.payTypeName}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4">无订单数据!</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>

        <input type="hidden" id="sessionId" value="<%= request.getSession().getId() %>" />

    </div>
</div>

<%@ include file="../footer.jsp"%>
<script type="text/javascript" src="${STATIC_URL}/static/js/dialog.js"></script>
<script type="text/javascript">
    $(function(){
        /* 支付方式单选按钮 */
        $('.radio-select label').click(function () {
            $(this).parent('div').find('.radio-skin').removeClass('radio-skin-selected');
            $(this).find('i:first').addClass('radio-skin-selected');
            var _payTypeId = $(this).attr("payTypeId");
            console.info("_payTypeId=" + _payTypeId);
            $("#payTypeId").val(_payTypeId);
        });
    });

    function pay(){
        var flowIds = "";
        $("input[name='flowId']").each(function(){
            if(this.value != null && this.value != "" && typeof this.value != "undefined"){
                if(flowIds == ""){
                    flowIds +=  this.value
                }else{
                    flowIds +=  + "," + this.value
                }
            }
        });
        var payTypeId = $("#payTypeId").val();
        if(payTypeId == null || payTypeId == "" || typeof  payTypeId == "undefined"){
            alert("请选择在线支付方式");
            return;
        }

        window.open("${PAY_DOMAIN}/orderPay/pay?flowIds="+flowIds+"&payTypeId="+payTypeId);
        setTimeout("openDialog(\""+flowIds+"\")","50");

        <%--var _url = "${PAY_DOMAIN}/orderPay/validateBankCard?flowIds="+flowIds+"&payTypeId="+payTypeId;--%>
        <%--$.ajax({--%>
            <%--url: _url,--%>
            <%--async:false,--%>
            <%--type:"get",--%>
            <%--dataType:"json",   //返回参数类型--%>
            <%--contentType :"application/json",   //请求参数类型--%>
            <%--beforeSend:function(request){--%>
                <%--request.setRequestHeader("mySessionId",$("#sessionId").val());--%>
            <%--},--%>
            <%--success:function(data){--%>
                <%--console.info(data);--%>
                <%--if("200" === data.statusCode){--%>
                    <%--window.open("${PAY_DOMAIN}/orderPay/pay?flowIds="+flowIds+"&payTypeId="+payTypeId);--%>
                    <%--setTimeout("openDialog(\""+flowIds+"\")","50");--%>
                <%--}else{--%>
                    <%--new Dialog({--%>
                        <%--title:'提示',--%>
                        <%--content:'<p class="mt60 f14">' + data.message + '</p>',--%>
                        <%--cancel:'取消',--%>
                        <%--ok:'确定'--%>
                    <%--});--%>
                <%--}--%>
            <%--},--%>
            <%--error:function(){--%>

            <%--}--%>
        <%--})--%>

    }

    /**
     * 查询订单支付状态
     * 如果订单已支付，返回true; 否则返回false;
     * @param _flowIds
     */
    function checkOrderPayedStatus(_flowIds){
        var _url = "${PAY_DOMAIN}/orderPay/checkOrderPayedStatus?flowIds=" + _flowIds ;
        var payedStatus = "false";
        $.ajax({
            url: _url,
            async: false,
            type: "get",
            dataType: "json",   //返回参数类型
            contentType: "application/json",   //请求参数类型
            beforeSend:function(request){
                request.setRequestHeader("mySessionId",$("#sessionId").val());
            },
            success: function (data) {
                if(data != null && "200" == data.statusCode){
                    payedStatus = data.payedStatus;
                }
            }
        });
        return "true" == payedStatus;

    }

    function openDialog(flowIds){
        new Dialog({
            title:'支付结果',
            content:'<p class="mt60 f14">请您在新打开的支付平台页面进行支付，支付完成前请不要关闭该窗口</p>',
            cancel:'支付遇到问题',
            ok:'已完成支付',
            afterOk:function(){ //已完成支付
                console.log('ok call back ..');
                //发送ajax请求，查询订单是否支付完成
                if(!checkOrderPayedStatus(flowIds)){
                    //若没有支付完成，弹框提示
                    new Dialog({
                        title: '提示',
                        content: '<p class="mt60 f14">您的订单未支付成功，请在订单中心重新支付!</p>',
                        ok: '确定',
                        afterOk:function(){
                            //跳到采购订单管理页
                            window.location.href = "http://oms.yaoex.com/order/buyerOrderManage";
                        }
                    });
                }else{
                    //跳到采购订单管理页
                    window.location.href = "http://oms.yaoex.com/order/buyerOrderManage";
                }



            },
            afterClose:function(){ //支付遇到问题
                console.log('close call back');
                //发送ajax请求，查询订单是否支付完成
                if(!checkOrderPayedStatus(flowIds)){
                    //若没有支付完成，弹框提示
                    new Dialog({
                        title: '提示',
                        content: '<p class="mt60 f14">您的订单未支付成功，请在订单中心再次支付!</p>',
                        ok: '确定',
                        afterOk:function(){
                            //跳到采购订单管理页
                            window.location.href = "http://oms.yaoex.com/order/buyerOrderManage";
                        }
                    });
                }else{
                    new Dialog({
                        title: '提示',
                        content: '<p class="mt60 f14">您已支付成功，点击确定跳转到采购订单管理页!</p>',
                        ok: '确定',
                        afterOk:function(){
                            //跳到采购订单管理页
                            window.location.href = "http://oms.yaoex.com/order/buyerOrderManage";
                        }
                    });
                }
            }
        });
    }
</script>
</body>
</html>