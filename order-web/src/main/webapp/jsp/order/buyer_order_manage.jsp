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
                        <li><a data-toggle="tab" onclick="changeStatus('1');" name="statusCount">待付款</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('2');" name="statusCount">待发货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('3');" name="statusCount">待收货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('4');" name="statusCount">拒收中</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('5');" name="statusCount">补货中</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('7');" name="statusCount">已完成</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('6');" name="statusCount">已取消</a></li>
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
                            <div class="pager" id="J_pager"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="myModalReceipt" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">确认收货</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <form id="upForm">
                        <div class="form-group">
                            <label for="scope" class="col-xs-3 control-label">结算订单金额:</label>
                            <div class="col-xs-5 control-label text-left"></div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-3 control-label"><em>*</em>实际结算金额:</label>
                            <div class="col-xs-5"><input name="refunSettlementMoney" type="text" class="form-control" /></div>
                            <div class="col-xs-4 control-label text-left">元</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-3 control-label">应付实付差异:</label>
                            <div class="col-xs-5 control-label text-left"></div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-3 control-label">备注:</label>
                            <div class="col-xs-5">
                                <textarea class="form-control" name="remark" rows="3" cols="3"></textarea>
                            </div>
                            <input name="orderSettlementId" type="hidden"  />
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
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


