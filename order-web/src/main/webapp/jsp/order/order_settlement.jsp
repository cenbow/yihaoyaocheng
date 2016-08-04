<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta charset="UTF-8">
    <title>基本信息完善</title>
    <!--#include file="header.asp" -->
    <!--#include file="sidebar.asp" -->
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/sidebar.js"></script>
    <%@ include file="../config.jsp"%>
    <link rel="Shortcut Icon" href="${ctx }/static/images/enterprise_new/yjs.ico">

</head>
<body>
<!--框架右侧内容 start-->
<div id="main-content" class="main-content">
    <div class="wrapper">
        <div class="qy_basenews">
            <div class="row no-margin">
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>首页</a></li>
                    <li class="active">商品清单</li>
                </ol>
            </div>
            <div class="row choseuser border-gray">
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">采购商区域</label>
                        <div class="col-xs-3">
                            <select class="form-control width-80"><option>省份</option><option>1</option><option>1</option></select>
                            <select class="form-control width-80"><option>城市</option><option>1</option><option>1</option></select>
                            <select class="form-control width-80"><option>区/县</option><option>1</option><option>1</option></select>
                        </div>
                        <label for="scope" class="col-xs-2 control-label">采购商 </label>
                        <div class="col-xs-3">
                            <input type="text" class="form-control" id="carnum" name="carnum" placeholder="">
                        </div>
                        <div class="col-xs-2"></div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">业务类型</label>
                        <div class="col-xs-3">
                            <select class="form-control"><option>请选择</option></select>
                        </div>
                        <label for="scope" class="col-xs-2 control-label">订单号</label>
                        <div class="col-xs-3">
                            <input type="text" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">支付方式</label>
                        <div class="col-xs-3">
                            <select class="form-control"><option>请选择</option></select>
                        </div>
                        <label for="scope" class="col-xs-2 control-label">结算状态</label>
                        <div class="col-xs-3">
                            <select class="form-control"><option>请选择</option></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">下单时间</label>
                        <div class="col-xs-3">
                            <div class="input-group input-large">
                                <input type="text" class="form-control Wdate border-right-none" onclick="WdatePicker()">
                                <span class="input-group-addon">至</span>
                                <input type="text" class="form-control Wdate border-left-none" onclick="WdatePicker()">
                            </div>
                            <p class="padding-t-10">[  <a class="blue">最近三天</a>   <a class="blue">最近1周</a>   <a class="blue">最近1年</a> ]</p>
                        </div>
                        <div class="col-xs-5 text-right"><button class="btn btn-info">搜索</button></div>
                    </div>
                </div>
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
                                        <col style="width: 15%;" />
                                        <col style="width: 15%;" />
                                        <col style="width: 15%;" />
                                        <col style="width: 15%;" />
                                        <col style="width: 5%;" />
                                        <col style="width: 5%;" />
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th>订单号</th>
                                        <th>支付方式</th>
                                        <th>业务类型</th>
                                        <th>采购商</th>
                                        <th>下单时间</th>
                                        <th>结算时间</th>
                                        <th>结算订单金额(元)</th>
                                        <th>结算状态</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>ZXD20160714</td>
                                        <td>线下转账</td>
                                        <td>销售应付</td>
                                        <td>上海国邦医药邦医药...</td>
                                        <td>2016-10-10 02:12:12</td>
                                        <td>2016-10-10 02:12:12</td>
                                        <td>500.00</td>
                                        <td>已结算</td>
                                        <td><button type="button" class="btn btn-info btn-sm editbtn">付款</button></td>
                                    </tr>
                                    <tr>
                                        <td>ZXD20160714</td>
                                        <td>线下转账</td>
                                        <td>销售应付</td>
                                        <td>上海国邦医药邦医药...</td>
                                        <td>2016-10-10 02:12:12</td>
                                        <td>2016-10-10 02:12:12</td>
                                        <td>500.00</td>
                                        <td>已结算</td>
                                        <td><button type="button" class="btn btn-info btn-sm editbtn">付款</button></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="pager" id="J_pager" current="3" total="20" url="http://www.baidu.com"><a href="javascript:void(0)" class="pager_prev">上一页</a><a href="javascript:void(0)" class="pager_item">1</a><a href="javascript:void(0)" class="pager_item">2</a><a href="javascript:void(0)" class="pager_item active">3</a><a href="javascript:void(0)" class="pager_item">4</a><a href="javascript:void(0)" class="pager_item">5</a><a href="javascript:void(0)" class="pager_item">6</a><a href="javascript:void(0)" class="pager_item">7</a><a href="javascript:void(0)" class="pager_item">8</a><span class="pager_dot">...</span><a href="javascript:void(0)" class="pager_item">20</a><a href="javascript:void(0)" class="pager_next">下一页</a><span class="page_total">共<em>20</em>页</span><label class="form_pageJump"><span>到<input type="text" name="page" class="input_item input_item_shortest page-num" autocomplete="off" id="page-num0">页</span><a href="javascript:void(0)" class="btn_blue btn_submit" data-form-button="submit">确定</a></label></div>
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
                <h4 class="modal-title" id="myModalLabel">付款</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">结算订单金额</label>
                        <div class="col-xs-5 control-label text-left">500.00  元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label"><em>*</em>实际结算金额</label>
                        <div class="col-xs-5"><input type="text" class="form-control" /></div>
                        <div class="col-xs-4 control-label text-left">元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">应付实付差异</label>
                        <div class="col-xs-5 control-label text-left">-100.00  元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">备注</label>
                        <div class="col-xs-5">
                            <textarea class="form-control" rows="3" cols="3"></textarea>
                        </div>
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
<!--#include file="footer.asp" -->
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
<!--#include file="footer.asp" -->
<script type="text/javascript"
        src="http://static.yaoex.com/jsp/common/footer.js"></script>
</body>
<script id="helpDoc" type="text/html">
    <div class="form-group padding-20">
        {{each helpDocs as value i}}
        <div class="col-xs-6 clearfix padding-tb-10">
            <span class="fl"><i class="fa fa-file-o"></i>&nbsp;{{value.title}}</span>
            <span class="fr"><a class="m-l-10 eyesee" href="{{value.fileUrl}}"><i class="fa fa-download"></i>下载</a></span>
        </div>
        {{/each}}
    </div>
</script>

</html>


