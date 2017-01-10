<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>结算管理-应收</title>
    <script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/sidebar.js"></script>
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
                    <li class="active">结算应收管理</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <form id="searchForm" method="POST" action="${ctx}/export/exportOrderSettlement/t1">
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
                        </div>
                        <div class="form-group">
	                        <label for="scope" class="col-xs-2 control-label">采购商 </label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="carnum" name="custName" placeholder="">
                            </div>
                            <label for="scope" class="col-xs-2 control-label">原订单号</label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" name="orgFlowId"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">业务类型</label>
                            <div class="col-xs-3">
                                <select class="form-control" name="businessType">
                                        <option value="-1">全部</option>
                                        <option value="1">销售货款</option>
                                        <option value="2">退货货款</option>
                                        <option value="3">拒收退款</option>
                                        <option value="4">取消订单退款</option>
                                        <option value="5">取消发货退款</option>
                                </select>
                            </div>
                            <label for="scope" class="col-xs-2 control-label">结算状态</label>
                            <div class="col-xs-3">
                                <select class="form-control" name="confirmSettlement">
                                    <option value="-1">全部</option>
                                    <option value="0">未结算</option>
                                    <option value="2">结算中</option>
                                    <option value="1">已结算</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                        	<label for="scope" class="col-xs-2 control-label">支付方式</label>
                            <div class="col-xs-3">
                                <select class="form-control" name="payType">
                                    <option value="-1">全部</option>
                                    <c:forEach var="item" items="${payTypeName }" varStatus="itemStatus" >
                                    	<option value="${item.payType }">${item.payTypeName }</option>
							  		</c:forEach>
                                </select>
                            </div>
                            <label for="scope" class="col-xs-2 control-label">支付平台</label>
                            <div class="col-xs-3">
                            	<select class="form-control" name="payTypeId">
                                    <option value="-1">全部</option>
                                    <c:forEach var="item" items="${payName }" varStatus="itemStatus" >
                                    	<option value="${item.payTypeId }">${item.payName }</option>
							  		</c:forEach>
                                </select>
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
                                <p class="padding-t-10">[  <a class="blue">最近三天</a>   <a class="blue">最近1周</a>   <a class="blue">最近1月</a> ]</p>
                            </div>
                            <div class="col-xs-5 text-left">
                                <input type="button" class="btn btn-info btn-search" value="搜索">
                                <input type="button" class="btn btn-info btn-export" value="导出">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="row margin-t-10">
                <div class="col-xs-12">
                    <div class="panel">
                        <div class="panel-body">
                            <div class="row">
                                <table class="table table-box">
                                    <colgroup>
                                        <col style="width: 10%;" />
                                        <col style="width: 10%;" />
                                        <col style="width: 10%;" />
                                        <col style="width: 6%;" />
                                        <col style="width: 7%;" />
                                        <col style="width: 7%;" />
                                        <col style="width: 12%;" />
                                        <col style="width: 7%;" />
                                        <col style="width: 7%;" />
                                        <col style="width: 7%;" />
                                        <col style="width: 7%;" />
                                        <col style="width: 5%;" />
                                        <col style="width: 5%;" />
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th>原订单号</th>
                                        <th>异常流程订单号</th>
                                        <th>结算流水号</th>
                                        <th>业务类型</th>
                                        <th>支付方式</th>
                                        <th>支付平台</th>
                                        <th>采购商</th>
                                        <th>下单时间</th>
                                        <th>结算时间</th>
                                        <th>应结算金额(元)</th>
                                        <th>实际结算金额(元)</th>
                                        <th>结算状态</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                            <div class="pager" id="J_pager" ></div>
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
                <button type="button" class="btn btn-danger" data-dismiss="modal">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
<%@ include file="../common_footer.jsp" %>
<script>
$(".editbtn").click(function(){
$("#myModal1").modal();
});
$("#delete").click(function(){
alertModal("确定要删除吗？");
});
</script>
<script type="text/javascript" src="${ctx}/static/js/area_data.js"></script>
<script type="text/javascript" src="${ctx}/static/js/area_select.js"></script>
<script type="text/javascript" src="${ctx }/static/js/order/order_settlement_seller.js"></script>

</body>


</html>


