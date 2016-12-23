<%@ page import="com.yyw.yhyc.order.enmu.OnlinePayTypeEnum" %><%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/9/2
  Time: 13:30
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <p class="red f18">订单金额： ¥ ${order.orgTotal}</p>
        </div>
        <div class="pay-type mt30">
            <p class="pay-type-top">请选择在线支付方式：</p>
            <div class="radio-select">
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
                <input type="hidden" name="flowId" value="${order.flowId}">
            </div>
        </div>
        <div class="mt45 tc btn">
            <a href="javascript:pay();;" class="os-btn-pay">立即在线支付</a>
        </div>
    </div>
</div>
<input type="hidden" id="sessionId" value="<%= request.getSession().getId() %>" />

<%@ include file="../footer.jsp"%>
<script type="text/javascript" src="${STATIC_URL}/static/js/dialog.js"></script>
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

    /**
     * “立即支付”按钮 触发事件
     */
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


        var _url = "${PAY_DOMAIN}/orderPay/validateBankCard?flowIds="+flowIds+"&payTypeId="+payTypeId;
        $.ajax({
            url: _url,
            async:false,
            type:"get",
            dataType:"json",   //返回参数类型
            contentType :"application/json",   //请求参数类型
            beforeSend:function(request){
                request.setRequestHeader("mySessionId",$("#sessionId").val());
            },
            success:function(data){
                console.info(data);
                if("200" === data.statusCode){
                    window.open("${PAY_DOMAIN}/orderPay/pay?flowIds="+flowIds+"&payTypeId="+payTypeId);
                    setTimeout("openDialog(\""+flowIds+"\")","50");
                }else{
                    new Dialog({
                        title:'提示',
                        content:'<p class="mt60 f14">' + data.message + '</p>',
                        cancel:'取消',
                        ok:'确定'
                    });
                }
            },
            error:function(){

            }
        })
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