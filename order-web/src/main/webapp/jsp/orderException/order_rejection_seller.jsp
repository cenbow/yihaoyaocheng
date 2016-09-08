<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>卖家拒收订单管理</title>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/sidebar.js"></script>
    <%@ include file="../config.jsp"%>
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
                    <li class="active">卖家拒收订单管理</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <form id="searchForm">
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">采购商区域</label>
                            <div class="col-xs-3">
                                <select class="form-control width-80" name="province" id="province">
                                    <option value="-1">省份</option>
                                </select>
                                <select class="form-control width-80" name="city" id="city">
                                    <option value="-1">城市</option>
                                </select>
                                <select class="form-control width-80" name="area" id="area">
                                    <option value="-1">区/县</option>
                                </select>
                            </div>
                            <label for="scope" class="col-xs-2 control-label">采购商 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="carnum" name="custName" placeholder="">
                            </div>
                            <div class="col-xs-2"></div>
                        </div>
                        <div class="form-group">

                            <label for="scope" class="col-xs-2 control-label">拒收订单号</label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" name="exceptionOrderId"/>
                            </div>
                            <label for="scope" class="col-xs-2 control-label">原订单号</label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" name="flowId"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">下单时间</label>
                            <div class="col-xs-3">
                                <div class="input-group input-large">
                                    <input type="text" name="startTime" class="form-control Wdate border-right-none" onclick="WdatePicker()">
                                    <span class="input-group-addon">至</span>
                                    <input type="text" name="endTime" class="form-control Wdate border-left-none" onclick="WdatePicker()">
                                </div>
                            </div>
                            <div class="col-xs-5 text-right">
                                <input type="button" class="btn btn-info" value="搜索">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row margin-t-10">
                <div class="col-xs-12">
                    <ul id="myTab" class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab"  >全部订单</a></li>
                        <li><a data-toggle="tab"  name="statusCount">待确认</a></li>
                        <li><a data-toggle="tab" name="statusCount">退款中</a></li>
                        <li><a data-toggle="tab"  name="statusCount">已完成</a></li>
                        <li><a data-toggle="tab"  name="statusCount">已关闭</a></li>
                    </ul>
                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="home">
                            <div class="clearfix padding-tb-20">
                                <div class="fl padding-t-10"><span class="margin-r-20">订单数：<span id="orderCount"></span></span><span class="red">订单金额：<span id="orderTotalMoney"></span></span></div>
                            </div>
                            <table class="table table-box">
                                <colgroup>
                                    <col style="width: 20%;"/>
                                    <col style="width: 20%;"/>
                                    <col style="width: 25%;"/>
                                    <col style="width: 15%;"/>
                                    <col style="width: 15%;"/>
                                    <col style="width: 5%;"/>
                                    <%--<col style="width: 15%;"/>--%>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>拒收订单号</th>
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
<div class="modal fade" id="myModalOperate" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">付款</h4>
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
<div class="modal fade" id="myModalDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">退款详情</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">结算订单金额:</label>
                        <div class="col-xs-5 control-label text-left"></div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">实际结算金额:</label>
                        <div class="col-xs-5 control-label text-left"></div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">应付实付差异:</label>
                        <div class="col-xs-5 control-label text-left"></div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">备注:</label>
                        <div class="col-xs-5 control-label text-left"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="http://static.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/js/My97DatePicker/WdatePicker.js"></script>
<script>
$(".editbtn").click(function(){
$("#myModal1").modal();
});
$("#delete").click(function(){
alertModal("确定要删除吗？");
});
</script>
<script type="text/javascript" src="http://static.yaoex.com/jsp/common/footer.js"></script>
<script type="text/javascript" src="${ctx }/static/js/pager.js"></script>
<script type="text/javascript" src="${ctx }/static/js/jquery.form.3.51.0.js"></script>
<script type="text/javascript" src="${ctx }/static/js/orderException/order_rejection_seller.js"></script>
<script type="text/javascript" src="${ctx }/static/js/common.js"></script>
<script type="text/javascript" src="${ctx }/static/js/area_data.js"></script>
</body>


</html>


