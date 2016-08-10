<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>采购订单管理</title>
    <!--#include file="header.asp" -->
    <!--#include file="sidebar.asp" -->
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/sidebar.js"></script>
    <%@ include file="../config.jsp" %>
    <link rel="Shortcut Icon" href="${STATIC_URL}/static/images/enterprise_new/yjs.ico">

</head>
<body>
<!--框架右侧内容 start-->
<div id="main-content" class="main-content">
    <div class="wrapper">
        <div class="qy_basenews">
            <div class="row no-margin">
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>首页</a></li>
                    <li class="active">采购订单管理</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <form>
                    <input type="hidden" name="orderStatus" value=""/>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">

                            <label for="flowId" class="col-xs-2 control-label">订单号 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="flowId" name="flowId" placeholder="">
                            </div>

                            <label for="supplyName" class="col-xs-2 control-label">供应商 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="supplyName" name="supplyName"
                                       placeholder="">
                            </div>

                        </div>
                        <div class="form-group">
                            <label for="payType" class="col-xs-2 control-label">支付方式</label>
                            <div class="col-xs-3">
                                <select class="form-control" name="payType" id="payType">
                                    <option value="">全部</option>
                                    <option value="1">在线支付</option>
                                    <option value="2">账期支付</option>
                                    <option value="3">线下支付</option>
                                </select>
                            </div>
                            <label class="col-xs-2 control-label">下单时间</label>
                            <div class="col-xs-3">
                                <div class="input-group input-large">
                                    <input type="text" name="createBeginTime"
                                           class="form-control Wdate border-right-none" onclick="WdatePicker()">
                                    <span class="input-group-addon">至</span>
                                    <input type="text" name="createEndTime" class="form-control Wdate border-left-none"
                                           onclick="WdatePicker()">
                                </div>
                                <p class="padding-t-10">[ <a class="blue">最近三天</a> <a class="blue">最近1周</a> <a
                                        class="blue">最近1月</a> ]</p>
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
                    <input type="button" class="btn btn-info" onclick="changeStatus('');" value="全部订单">
                    <input type="button" class="btn btn-info" onclick="changeStatus('1');" value="待付款"><span name="statusCount"></span>
                    <input type="button" class="btn btn-info" onclick="changeStatus('2');" value="待发货"><span name="statusCount"></span>
                    <input type="button" class="btn btn-info" onclick="changeStatus('3');" value="待收货"><span name="statusCount"></span>
                    <input type="button" class="btn btn-info" onclick="changeStatus('4');" value="拒收中"><span name="statusCount"></span>
                    <input type="button" class="btn btn-info" onclick="changeStatus('5');" value="补货中"><span name="statusCount"></span>
                    <input type="button" class="btn btn-info" onclick="changeStatus('7');" value="已完成"><span name="statusCount"></span>
                    <input type="button" class="btn btn-info" onclick="changeStatus('6');" value="已取消"><span name="statusCount"></span>
                </div>
            </div>
            <div class="row margin-t-10">
                <div class="col-xs-12">
                    订单金额:<span id="orderTotalMoney"></span>
                    订单数:<span id="orderCount"></span>
                </div>
            </div>
            <div class="row margin-t-10">
                <div class="col-xs-12">
                    <div class="panel">
                        <div class="panel-body">
                            <div class="row">
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
                                        <th>订单号</th>
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
                            </div>
                            <div class="pager" id="J_pager"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--#include file="footer.asp" -->
<script type="text/javascript" src="http://static.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/js/My97DatePicker/WdatePicker.js"></script>
<!--#include file="footer.asp" -->
<script type="text/javascript" src="http://static.yaoex.com/jsp/common/footer.js"></script>
<script type="text/javascript" src="${ctx }/static/js/pager.js"></script>
<script type="text/javascript" src="${ctx }/static/js/jquery.form.3.51.0.js"></script>
<script type="text/javascript" src="${ctx }/static/js/order/buyer_order_manage.js"></script>

</body>


</html>


