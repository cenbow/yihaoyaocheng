<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>换货订单管理</title>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/sidebar.js"></script>
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
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>销售订单管理</a></li>
                    <li class="active">换货订单管理</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <form id="form0">
                    <input type="hidden" name="orderStatus" value=""/>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">

                            <label for="exceptionOrderId" class="col-xs-2 control-label">换货订单号 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="exceptionOrderId" name="exceptionOrderId" placeholder="">
                            </div>

                            <label for="flowId" class="col-xs-2 control-label">原始订单号 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="flowId" name="flowId"
                                       placeholder="">
                            </div>

                        </div>
                        <div class="form-group">
                            <label for="supplyName" class="col-xs-2 control-label">供应商 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="supplyName" name="supplyName"
                                       placeholder="">
                            </div>
                            <label class="col-xs-2 control-label">换货时间</label>
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
                                <input type="button" class="btn btn-info" value="搜索">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row margin-t-10">
                <div class="col-xs-12">
                    <ul id="myTab" class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" onclick="changeStatus('');" >全部订单</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('1');" name="statusCount1">待确认</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('4');" name="statusCount4">待买家发货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('5');" name="statusCount5">待卖家收货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('6');" name="statusCount6">待卖家发货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('7');" name="statusCount7">待买家收货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('8');" name="statusCount8">已完成</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('3');" name="statusCount3">已关闭</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('2');" name="statusCount2">已取消</a></li>
                    </ul>

                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="home">
                            <div class="clearfix padding-tb-20">
                                <div class="fr padding-t-10"><span class="margin-r-20">订单数：<span id="orderCount"></span></span><span class="red">订单金额：<span id="orderTotalMoney"></span></span></div>
                            </div>
                            <table class="table table-box">
                                <colgroup>
                                    <col style="width: 25%;"/>
                                    <col style="width: 15%;"/>
                                    <col style="width: 25%;"/>
                                    <col style="width: 10%;"/>
                                    <col style="width: 15%;"/>
                                    <col style="width: 10%;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>换货订单号</th>
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
<script type="text/javascript" src="http://static.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/jsp/common/footer.js"></script>
<script type="text/javascript" src="${ctx }/static/js/pager.js"></script>
<script type="text/javascript" src="${ctx }/static/js/jquery.form.3.51.0.js"></script>
<script type="text/javascript" src="${ctx }/static/js/orderException/seller_change_order_manage.js"></script>

</body>


</html>


