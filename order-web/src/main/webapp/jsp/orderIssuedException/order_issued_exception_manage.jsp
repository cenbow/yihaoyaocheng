<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>异常订单管理</title>
    <script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/sidebar.js"></script>
    <%@ include file="../config.jsp" %>
    <link rel="Shortcut Icon" href="${STATIC_URL}/static/images/enterprise_new/yjs.ico">

    <link href="${STATIC_URL}/static/css/common.css" rel="stylesheet"/>

</head>
<body>


<!--框架右侧内容 start-->
<div id="main-content" class="main-content">
    <div class="wrapper">
        <div class="qy_basenews">
            <div class="row no-margin">
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>首页</a></li>
                    <li class="active">异常订单管理</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <form  action="${ctx }/orderIssuedException/export" method="post" id="form0">
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">订单编码</label>

                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="flowId" name="flowId"/>
                            </div>
                            <label for="scope" class="col-xs-2 control-label">下单时间</label>

                            <div class="col-xs-3">
                                <div class="input-group input-large">
                                    <input type="text" id="createBeginTime" name="createBeginTime"
                                           class="form-control Wdate border-right-none" onclick="WdatePicker()"/>
                                    <span class="input-group-addon">至</span>
                                    <input type="text" id="createEndTime" name="createEndTime"
                                           class="form-control Wdate border-left-none" onclick="WdatePicker()"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">异常类型</label>

                            <div class="col-xs-3">
                                <select class="form-control" id="exceptionType" name="exceptionType">
                                    <option value="">请选择</option>
                                    <option value="0">下发超时未返回</option>
                                    <option value="1">无关联企业用户</option>
                                    <option value="2">下发返回错误</option>
                                    <option value="3">下发失败</option>
                                </select>
                            </div>

                            <label for="scope" class="col-xs-2 control-label">支付方式</label>

                            <div class="col-xs-3">
                                <select class="form-control" name="payType" id="payType">
                                    <option value="">请选择</option>
                                    <option value="1">在线支付</option>
                                    <option value="2">账期支付</option>
                                    <option value="3">线下支付</option>
                                </select>
                            </div>

                        </div>

                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">供应商</label>

                            <div class="col-xs-3">
                                <input type="text" id="supplyName" name="supplyName" class="form-control"/>
                            </div>

                            <label for="custName" class="col-xs-2 control-label">采购商</label>

                            <div class="col-xs-3">
                                <input type="text" id="custName" name="custName" class="form-control"/>
                            </div>

                        </div>


                        <div class="form-group">
                            <div class="col-xs-10 text-right">
                                <button type="button" class="btn btn-info">查询</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="padding-t-20">
                <button type="button" class="btn btn-info margin-r-10 editbtn" onclick="document.getElementById('form0').submit();">&nbsp;导出&nbsp;</button>
                <button type="button" class="btn btn-info margin-r-10" onclick="batchModify(1);">&nbsp;下发&nbsp;</button>
                <button type="button" class="btn btn-info margin-r-10" onclick="batchModify(2);">标记完成</button>
            </div>
            <div class="row margin-t-10">
                <div class="col-xs-12">
                    <div class="panel">
                        <div class="panel-body">
                            <div class="row">
                                <table class="table table-box">
                                    <colgroup>
                                        <col style="width: 5%;"/>
                                        <col style="width: 10%;"/>
                                        <col style="width: 8%;"/>
                                        <col style="width: 7%;"/>
                                        <col style="width: 8%;"/>
                                        <col style="width: 8%;"/>
                                        <col style="width: 7%;"/>
                                        <col style="width: 10%;"/>
                                        <col style="width: 5%;"/>
                                        <col style="width: 7%;"/>
                                        <col style="width: 7%;"/>
                                        <col style="width: 8%;"/>
                                        <col style="width: 10%;"/>
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><input type="checkbox" id="checkboxTitle" /></th>
                                        <th>订单编号</th>
                                        <th>下单时间</th>
                                        <th>供应商</th>
                                        <th>订单状态</th>
                                        <th>处理状态</th>
                                        <th>支付类型</th>
                                        <th>采购商</th>
                                        <th>收货人</th>
                                        <th>联系方式</th>
                                        <th>收货地址</th>
                                        <th>异常类型</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="showList">

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


<div class="modal fade" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:650px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">日志</h4>
            </div>
            <div class="modal-body">
                <table class="table table-box1">
                    <colgroup>
                        <col style="width: 30%;"/>
                        <col style="width: 40%;"/>
                        <col style="width: 30%;"/>
                    </colgroup>
                    <thead>
                    <tr>
                        <th>操作</th>
                        <th>操作人</th>
                        <th>操作时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width:500px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabe2">客户关联设置</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label class="col-xs-4 control-label">客户编码</label>
                        <div class="col-xs-6 control-label text-left" id="custCodeYc"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-4 control-label">客户名称</label>
                        <div class="col-xs-6 control-label text-left" id="custNameYc"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-4 control-label">本公司客户编码</label>

                        <div class="col-xs-6">
                            <input class="form-control" type="text" id="custCodeErp" name="custCodeErp"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-4 control-label">本公司客户名称</label>

                        <div class="col-xs-6">
                            <input class="form-control" type="text" id="custNameErp" name="custNameErp"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-4 control-label">订单下发</label>

                        <div class="col-xs-8">
                            <label class="checkbox-inline"><input name="payTypeErp" type="checkbox" value="1">在线支付</label>
                            <label class="checkbox-inline"><input name="payTypeErp" type="checkbox" value="2">账期支付</label>
                            <label class="checkbox-inline"><input name="payTypeErp" type="checkbox" value="3">线下支付</label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <input type="button" class="btn btn-danger" onclick="relatedCustomers()" value="确定">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

</body>

<%@ include file="../common_footer.jsp" %>
<script type="text/javascript" src="${ctx }/static/js/orderIssuedException/order_issued_exception_manage.js"></script>
