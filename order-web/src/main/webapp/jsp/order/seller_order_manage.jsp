<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>销售订单管理</title>
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
                    <li class="active">销售订单管理</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <form action="${ctx }/order/exportOrder" method="post" id="form0">
                    <input type="hidden" id="orderStatus" name="orderStatus" value=""/>
                    <div class="form-horizontal padding-t-26">

                        <div class="form-group">
                            <label for="province" class="col-xs-2 control-label">采购商区域</label>
                            <div class="col-xs-3">
                                <select class="form-control width-80" name="province" id="province">
                                    <option value="">省份</option>
                                </select>
                                <select class="form-control width-80" name="city" id="city">
                                    <option value="">城市</option>
                                </select>
                                <select class="form-control width-80" name="district" id="district">
                                    <option value="">区域</option>
                                </select>
                            </div>

                            <label for="custName" class="col-xs-2 control-label">采购商 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="custName" name="custName"
                                       placeholder="">
                            </div>
                            <div class="col-xs-2"></div>

                        </div>

                        <div class="form-group">

                            <label for="payType" class="col-xs-2 control-label">支付方式</label>
                            <div class="col-xs-3">
                                <select class="form-control" name="payType" id="payType">
                                </select>
                            </div>

                            <label for="flowId" class="col-xs-2 control-label">订单号</label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="flowId" name="flowId" placeholder="">
                            </div>

                        </div>
                       <div class="form-group">
                        	<label for="adviser" class="col-xs-2 control-label">销售顾问</label>
                            <div class="col-xs-3">
                                <select class="form-control" name="adviserCode" id="adviserCode">
                                </select>
                            </div>
                            <label for="promotionName" class="col-xs-2 control-label">促销活动</label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="promotionName" name="promotionName" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 control-label">下单时间</label>
                            <div class="col-xs-3">
                                <div class="input-group input-large">
                                    <input type="text" name="createBeginTime" id="createBeginTime"
                                           class="form-control Wdate border-right-none" onclick="WdatePicker()">
                                    <span class="input-group-addon">至</span>
                                    <input type="text" name="createEndTime" id="createEndTime" class="form-control Wdate border-left-none"
                                           onclick="WdatePicker()">
                                </div>
                                <p class="padding-t-10">[ <a class="blue" onclick="selectDate(-3)">最近三天</a> <a class="blue" onclick="selectDate(-7)">最近1周</a> <a
                                        class="blue" onclick="selectDate(-30)">最近1月</a> ]</p>
                            </div>
                            <label class="col-xs-2 control-label"></label>
                            <div class="col-xs-3">
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
                                <div class="fl"><button type="button" class="btn btn-info" onclick="document.getElementById('form0').submit();">订单导出</button></div>
                                <div class="fr padding-t-10">
                                     <span class="margin-r-20" >
                                        <a href="http://his.fangkuaiyi.com" title="老平台订单查询">
                                            <span style="color: #fe5d51;font-weight: bold;text-decoration: underline">老平台订单查询</span>
                                        </a>
                                    </span>
                                    <span class="margin-r-20">
                                        订单数：<span id="orderCount"></span>
                                    </span>
                                    <span class="red">
                                        订单金额：<span id="orderTotalMoney"></span>
                                    </span>
                                </div>
                            </div>
                            <table class="table table-box">
                                <colgroup>
                                    <col style="width: 15%;"/>
                                    <col style="width: 16%;"/>
                                    <col style="width: 21%;"/>
                                    <col style="width: 16%;"/>
                                    <col style="width: 17%;"/>
                                    <col style="width: 15%;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>订单号</th>
                                    <th>下单时间</th>
                                    <th>采购商</th>
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
<div class="modal fade" id="myModalOperate" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <input type="hidden" id="orderId"/>
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">取消订单</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="cancelResult" class="col-xs-3 control-label">备注:</label>
                        <div class="col-xs-5 control-label text-left">
                            <textarea type="text" class="form-control" id="cancelResult" name="cancelResult"
                                      placeholder="" maxlength="200"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" onclick="doCancle();">确定</button>
                    <button type="button" id="closeM" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
            </div>
        </div>
    </div>
</div>

   <!-- 当发货是部分发货的时候该对话框弹出 -->
<div class="modal fade" id="myConfirmOtherSendMessage" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">发货</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <input type="hidden" id="hiddenFileName">
                        <input type="hidden" id="hiddenReceiverAddressId">
                        <input type="hidden" id="hiddenDeliveryContactPerson">
                        <input type="hidden" id="hiddenDeliveryExpressNo">
                        <input type="hidden" id="hiddenDeliveryMethod">
                        <input type="hidden" id="hiddenDeliveryDateHidden">
                        <input type="hidden" id="hiddenSendFlowId">
                        <input type="hidden" id="hiddenOrderType" value="1">
                        <label for="cancelResult" class="col-xs-6 control-label">你此次为部分发货,<span style="color: red">剩余商品是否补发货</span>:</label>
                        <div class="col-xs-6 control-label text-left">
                               <input name="selectPartDeliverty" value="1" type="radio">是
                               <input name="selectPartDeliverty" value="0" type="radio">否(不发货商品将进行退款)
                        </div>
                    </div>
                    
                   <div class="form-group">
                        <label for="cancelResult" class="col-xs-3 control-label"><span style="color: red">*</span>说明:</label>
                        <div class="col-xs-6 control-label text-left">
                         <textarea type="text" class="form-control" id="partComent" name="partComent" placeholder="" maxlength="200"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" onclick="partDeliveryConfirm();">确定</button>
                    <button type="button" id="closeM" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
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
<form action="" id="exportTemplateForm" method="post">
	<input type="hidden" name="batchTemplateFlowId" id="batchTemplateFlowId">
	<input type="hidden" name="orderType" id="orderTypeTemplate">
</form>
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
                         <input type="hidden" id="deliveryDateHidden" name="deliveryDateHidden">
                        <input type="hidden" id="sendFlowId" name="flowId">
                        <input type="hidden" id="orderType" name="orderType" value="1">
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label"><em>*</em>发货仓库</label>
                            <div class="col-xs-10 send_goods" id="warehouse">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 control-label">批号导入</label>
                            <div class="col-xs-7">
                                <input type="file" id="excelFile" name="excelFile"   onchange="closeFileInput(this)"  />
                                <p class="padding-t-10"><a class="m-l-10 eyesee" id="batchTemplateExport"><i class="fa fa-download"></i>&nbsp;批号导入模版下载</a></p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label"><em>*</em>配送方式</label>
                            <div class="col-xs-9 border-gray no-padding">
                                <div class="border-bottom padding-b-10">
                                    <label class="radio-inline margin-l-10" href="#one1" onclick="totab(1)">
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
                                                <input type="text" class="form-control" id="deliveryContactPerson1" maxlength="20"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="scope" class="col-xs-3 control-label">联系人电话</label>
                                            <div class="col-xs-8">
                                                <input type="text" class="form-control" id="deliveryExpressNo1" maxlength="15" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="padding-t-20 tab-pane fade" id="one2">
                                        <div class="form-group">
                                            <label for="scope" class="col-xs-3 control-label">运输公司</label>
                                            <div class="col-xs-8">
                                                <input type="text" class="form-control" id="deliveryContactPerson2"  maxlength="30" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="scope" class="col-xs-3 control-label">物流单号</label>
                                            <div class="col-xs-8">
                                                <input type="text" class="form-control" id="deliveryExpressNo2" maxlength="50"  />
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
 <!-- 部分发货的收发货物清单 -->
<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1000px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel1">收货商品清单</h4>
            </div>
            <div class="modal-body">
                <table class="table table-box2">
                    <colgroup>
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                        <col style="width: 10%;">
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                        <col style="width: 8%;">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>订单行号</th>
                        <th>商品编码</th>
                        <th>批号</th>
                        <th>有效期至</th>
                        <th>商品名</th>
                        <th>通用名</th>
                        <th>规格</th>
                        <th>剂型</th>
                        <th>生产企业</th>
                        <th>采购数量</th>
                        <th>发货数量</th>
                        <th>收货数量</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                
                   <div class="form-group" style="display: none;" id="showProductMoneyDiv">
	                  <label for="scope" class="col-xs-8 control-label"></label>
	                  <label for="scope" class="col-xs-4 control-label">未发货商品金额:&yen<span id="showMoney"></span></label>
                  </div>
                 
              
                <div class="pager" id="J_pager2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
 </div>
 
<%@ include file="../common_footer.jsp" %>
<script type="text/javascript" src="${ctx }/static/js/order/seller_order_manage.js"></script>
<script type="text/javascript" src="${ctx }/static/js/area_data.js"></script>
</body>


</html>


