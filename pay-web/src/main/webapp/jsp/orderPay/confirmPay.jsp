<%@ page import="com.yyw.yhyc.order.enmu.OnlinePayTypeEnum" %><%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/9/2
  Time: 13:30
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <%@ include file="../config.jsp"%>
    <title>在线支付点击付款页面-1号药城</title>
    <meta name="Keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="${STATIC_URL}/static/css/inside.css">
</head>
<body>

<%@ include file="../head.jsp"%>

<div class="wapper header">
    <h1 class="common-icon"><a href="#">1号药城 — 方便.快捷.第一</a></h1>
</div>
<div class="wapper order-to-pay">
    <div class="order-content">
        <div class="mt30 f14 ci-hank-info">
            <p>供应商：${order.supplyName}</p>
            <p>订单号：${order.flowId}</p>
            <p class="red f18">订单金额： ¥ ${order.orderTotal}</p>
        </div>
        <div class="pay-type mt30">
            <p class="pay-type-top">请选择在线支付方式：</p>
            <div class="radio-select">
                <%--<label payTypeId="<%=OnlinePayTypeEnum.MerchantBank.getPayTypeId()%>">--%>
                    <%--<i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-cmb"></i><br><span>--%>
                    <%--<i class="common-icon radio-tip"></i>需开通招商银行企业网银</span>--%>
                <%--</label>--%>
                    <label payTypeId="<%=OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId()%>">
                        <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-pay"></i><br><span>银联卡支付</span>
                    </label>
                    <label payTypeId="<%=OnlinePayTypeEnum.UnionPayB2C.getPayTypeId()%>">
                        <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-pay"></i><br><span class="pr">网银支付-B2C</span>
                    </label>
                    <label payTypeId="<%=OnlinePayTypeEnum.UnionPayB2B.getPayTypeId()%>">
                        <i class="inside-icon radio-skin"></i><i class="inside-icon banklogo-pay"></i><br><span class="pr">网银支付-B2B</span>
                    </label>
                    <label payTypeId="<%=OnlinePayTypeEnum.AlipayWeb.getPayTypeId()%>">
                        <i class="inside-icon radio-skin"></i><img src="${STATIC_URL}/static/images/alipay.png" alt=""><br><span class="pr"></span>
                    </label>
                <input type="hidden" id="payTypeId">
                <input type="hidden" name="flowId" value="${order.flowId}">
            </div>
        </div>
        <div class="mt45 tc btn">
            <a href="javascript:pay();;" class="os-btn-pay">立即在线支付</a>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp"%>
<script type="text/javascript">
    $(function(){
        /* 支付方式单选按钮 */
        $('.radio-select label').click(function () {
            $(this).parent('div').find('.radio-skin').removeClass('radio-skin-selected');
            $(this).find('i:first').addClass('radio-skin-selected');
            var _payTypeId = $(this).attr("payTypeId");
            $("#payTypeId").val(_payTypeId);
        });
    });

    function pay(){
        var flowIds = "";
        $("input[name='flowId']").each(function(){
            if(this.value != null && this.value != "" && typeof this.value != "undefined"){
                if(flowIds == ""){
                    flowIds +=  this.value + ","
                }else{
                    flowIds +=  this.value
                }
            }
        });
        var payTypeId = $("#payTypeId").val();
        if(payTypeId == null || payTypeId == "" || typeof  payTypeId == "undefined"){
            alert("请选择在线支付方式");
            return;
        }
        window.location.href = "${PAY_DOMAIN}/orderPay/pay?flowIds="+flowIds+"&payTypeId="+payTypeId;
    }
</script>

</body>
</html>