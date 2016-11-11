<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String flowId =  request.getParameter("flowId");
	flowId = flowId==null?"":flowId;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>补货订单管理</title>
    <script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/sidebar.js"></script>
    <%@ include file="../config.jsp" %>
    <link rel="Shortcut Icon" href="${STATIC_URL}/static/images/enterprise_new/yjs.ico">

    <link href="${STATIC_URL}/static/css/common.css" rel="stylesheet" />

</head>
<body>
<!--框架右侧内容 start-->
<div id="main-content" class="main-content">
    <div class="wrapper">
        <div class="qy_basenews">
            <div class="row no-margin">
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>首页</a></li>
                    <li class="active">补货订单管理</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <form id="form0">
                    <input type="hidden" name="orderStatus" value=""/>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">

                            <label for="exceptionOrderId" class="col-xs-2 control-label">补货订单号 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="exceptionOrderId" name="exceptionOrderId" placeholder="">
                            </div>

                            <label for="flowId" class="col-xs-2 control-label">原始订单号 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="flowId" name="flowId" value="<%=flowId%>"
                                       placeholder="">
                            </div>

                        </div>
                        <div class="form-group">
                            <label for="supplyName" class="col-xs-2 control-label">供应商 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="supplyName" name="supplyName"
                                       placeholder="">
                            </div>
                            <label class="col-xs-2 control-label">补货时间</label>
                            <div class="col-xs-3">
                                <div class="input-group input-large">
                                    <input type="text" name="startTime"
                                           class="form-control Wdate border-right-none" onclick="WdatePicker()">
                                    <span class="input-group-addon">至</span>
                                    <input type="text" name="endTime" class="form-control Wdate border-left-none"
                                           onclick="WdatePicker()">
                                </div>
                                <p class="padding-t-10">[ <a class="blue" onclick="selectDate(-3)">最近三天</a> <a class="blue" onclick="selectDate(-7)">最近1周</a> <a
                                        class="blue" onclick="selectDate(-30)">最近1月</a> ]</p>
                            </div>
                            <div class="col-xs-2 text-left">
                                <input type="button" class="btn btn-info" value="搜索">&nbsp;&nbsp;&nbsp;
                                <input type="button" class="btn btn-info" value="订单导出" id="export">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row margin-t-10">
                <div class="col-xs-12">
                    <ul id="myTab" class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" onclick="changeStatus('');" >全部</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('1');" name="statusCount">待确认</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('2');" name="statusCount">待发货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('3');" name="statusCount">待收货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('5');" name="statusCount">已完成</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('4');" name="statusCount">已关闭</a></li>
                    </ul>

                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="home">
                            <div class="clearfix padding-tb-20">
                                <div class="fr padding-t-10"><span class="margin-r-20">订单数：<span id="orderCount"></span></span><span class="red">订单金额：<span id="orderTotalMoney"></span></span></div>
                            </div>
                            <table class="table table-box">
                                <colgroup>
                                    <col style="width: 20%;"/>
                                    <col style="width: 15%;"/>
                                    <col style="width: 20%;"/>
                                    <col style="width: 15%;"/>
                                    <col style="width: 15%;"/>
                                    <col style="width: 15%;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>补货订单号</th>
                                    <th>下单时间</th>
                                    <th>供应商</th>
                                    <th>订单状态</th>
                                    <th>订单金额</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                            <div class="pager" id="J_pager"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form action="" id="exportForm" method="post">
	<input type="hidden" name="condition" id="condition">
	<input type="hidden" name="returnType" id="returnType" value="3">
</form>
<div class="modal fade" id="myModalConfirmReceipt" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1000px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel1">确认收货</h4>
            </div>
            <div class="modal-body">
                <form method="post" id="confirmReceiptForm" enctype="multipart/form-data">
                    <input type='hidden' name='flowId' id="crExceptionOrderId" >
                    <table class="table table-box2">
                        <colgroup>
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                            <col style="width: 10%;">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>订单行号</th>
                            <th>商品编码</th>
                            <th>批号</th>
                            <th>商品名</th>
                            <th>通用名</th>
                            <th>规格</th>
                            <th>剂型</th>
                            <th>生产企业</th>
                            <th>采购数量</th>
                            <th>确认收货数量</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="repConfirmReceipt()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common_footer.jsp" %>
<script type="text/javascript" src="${ctx }/static/js/orderException/buyer_replenishment_order_manage.js"></script>

</body>


</html>


