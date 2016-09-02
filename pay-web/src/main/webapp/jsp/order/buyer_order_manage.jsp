<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>采购订单管理</title>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/sidebar.js"></script>
    <%@ include file="../config.jsp" %>
    <link rel="Shortcut Icon" href="${STATIC_URL}/static/images/enterprise_new/yjs.ico">

    <link href="${STATIC_URL}/static/css/common.css" rel="stylesheet" />

</head>
<body>
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
                <form id="form1">
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
<div class="modal fade" id="myModalConfirmReceipt" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1000px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel1">确认收货</h4>
            </div>
            <div class="modal-body">
                <form method="post" id="confirmReceiptForm" enctype="multipart/form-data">
                    <input type='hidden' name='flowId' id="crflowId" >
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
                        <th>收货数量</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                    <div class="modal-body" id="bodyDiv" style="display: none">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="scope" class="col-xs-3 control-label">请选择对剩余商品的处理并确认，默认做为拒收处理：</label>
                                <div class="col-xs-8" style="padding-top: 2%;">
                                    <input type='hidden' name='list.returnType' id="returnType" >
                                    <input type="radio" checked="checked" name="ownw" value="4">拒收
                                    <input type="radio" name="ownw" value="3">补货
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="cancelResult" class="col-xs-3 control-label">拒收/补货说明:</label>
                                <div class="col-xs-5 control-label text-left">
                                    <textarea type="text" class="form-control" id="returnDesc" name="list.returnDesc"
                                      placeholder="" maxlength="200"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="confirmReceipt()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModalSalesReturn" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1000px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="SalesReturn">退换货</h4>
                <input type='hidden' name='flowId' id="curflowId" >
            </div>
            <div class="modal-body">
                <ul class="nav nav-tabs" id="myTab2">
                    <li class="active"><a  data-toggle="tab">退货</a></li>
                    <li><a name="statusCount"  data-toggle="tab">换货</a></li>
                </ul>
                <form method="post" id="myModalSalesReturnForm" enctype="multipart/form-data">
                    <table class="table table-box3">
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
                            <th>可退数量</th>
                            <th>退货数量</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    <div class="modal-body" id="bodyDiv2" >
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label for="cancelResult" class="col-xs-3 control-label">退货说明:</label>
                                <div class="col-xs-6 control-label text-left">
                                    <textarea type="text" class="form-control" id="returnDesc2" name="list.returnDesc"
                                              placeholder="" maxlength="200"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="confirmSaleReturn();">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="postponeOrder" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">延期收获</div>
            <div class="modal-body font-size-16 text-center" >
                每笔订单最多延期收货两次，确认延期收货吗？
            </div>
            <div class="modal-footer text-center">
                <input type="hidden" id="postponeOrderId">
                <button type="button" class="btn btn-default" onclick="postponeOrder();">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="http://static.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/jsp/common/footer.js"></script>
<script type="text/javascript" src="${ctx }/static/js/pager.js"></script>
<script type="text/javascript" src="${ctx }/static/js/jquery.form.3.51.0.js"></script>
<script type="text/javascript" src="${ctx }/static/js/order/buyer_order_manage.js"></script>
<script type="text/javascript" src="${ctx }/static/js/order/buyer_order_manage_return.js"></script>
</body>


</html>


