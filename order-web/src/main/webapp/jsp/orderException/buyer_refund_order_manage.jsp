<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>退货订单管理</title>
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
                    <li class="active">退货订单管理</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <form id="form0">
                    <input type="hidden" name="orderStatus" value=""/>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">

                            <label for="exceptionOrderId" class="col-xs-2 control-label">退货订单号 </label>
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
                            <label class="col-xs-2 control-label">退货时间</label>
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
                        <li class="active"><a data-toggle="tab" onclick="changeStatus('');" >全部</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('1');" name="statusCount">待确认</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('3');" name="statusCount">待买家发货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('5');" name="statusCount">待卖家收货</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('6');" name="statusCount">退款中</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('7');" name="statusCount">已完成</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('4');" name="statusCount">已关闭</a></li>
                        <li><a data-toggle="tab" onclick="changeStatus('2');" name="statusCount">已取消</a></li>
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
                                    <th>退货订单号</th>
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
</div>
<div class="modal fade" id="myModalSendDelivery" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">发货</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <form method="post" id="sendform" enctype="multipart/form-data">
                        <input type="hidden" id="receiverAddressId" name="receiverAddressId">
                        <input type="hidden" id="deliveryContactPerson" name="deliveryContactPerson">
                        <input type="hidden" id="deliveryExpressNo" name="deliveryExpressNo">
                        <input type="hidden" id="deliveryMethod" name="deliveryMethod">
                        <input type="hidden" id="sendFlowId" name="flowId">

                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label"><em>*</em>发货仓库</label>
                            <div class="col-xs-10 send_goods" id="warehouse">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label"><em>*</em>配送方式</label>
                            <div class="col-xs-9 border-gray no-padding">
                                <div class="border-bottom padding-b-10">
                                    <label class="radio-inline margin-l-10" href="#one1" onclick="totab(1)" >
                                        <input type="radio" checked="true"  name="ownw" id="ownw1" value="1">自有物流
                                    </label>
                                    <label class="radio-inline no-margin"  href="#one2" onclick="totab(2)">
                                        <input type="radio" name="ownw"  value="2" id="ownw2">第三方运输公司
                                    </label>
                                </div>
                                <div class="tab-content">
                                    <div class="padding-t-20 tab-pane fade in active" id="one1">
                                        <div class="form-group">
                                            <label for="scope" class="col-xs-3 control-label">预计到达时间</label>
                                            <div class="col-xs-8">
                                                <input type="text" name="deliveryDate" id="deliveryDate" class="form-control Wdate" onclick="WdatePicker()" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="scope" class="col-xs-3 control-label">联系人</label>
                                            <div class="col-xs-8">
                                                <input type="text" class="form-control" id="deliveryContactPerson1"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="scope" class="col-xs-3 control-label">联系人电话</label>
                                            <div class="col-xs-8">
                                                <input type="text" class="form-control" id="deliveryExpressNo1"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="padding-t-20 tab-pane fade" id="one2">
                                        <div class="form-group">
                                            <label for="scope" class="col-xs-3 control-label">运输公司</label>
                                            <div class="col-xs-8">
                                                <input type="text" class="form-control" id="deliveryContactPerson2" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="scope" class="col-xs-3 control-label">物流单号</label>
                                            <div class="col-xs-8">
                                                <input type="text" class="form-control" id="deliveryExpressNo2" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="sendDeliverysubmit()" >确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModalPrompt" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">温馨提示</h4>
            </div>
            <div class="modal-body">
                <div id="msgDiv">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%@ include file="../common_footer.jsp" %>
<script type="text/javascript" src="${ctx }/static/js/orderException/buyer_refund_order_manage.js"></script>

</body>


</html>


