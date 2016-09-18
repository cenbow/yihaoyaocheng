<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>库存管理</title>
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
          <li><a href="#"><i class="fa fa-map-marker fa-3"></i>商品库存</a></li>
          <li class="active">库存管理</li>
        </ol>
      </div>
      <div class="row choseuser border-gray">
        <form  id="form0">
        <div class="form-horizontal padding-t-26">
          <div class="form-group">
            <label for="scope" class="col-xs-2 control-label">本公司产品编码</label>
            <div class="col-xs-3">
              <input type="text" class="form-control"  id="productcodeCompany" name="productcodeCompany" />
            </div>
            <label for="scope" class="col-xs-2 control-label">通用名</label>
            <div class="col-xs-3">
              <input type="text" class="form-control" id="shortName" name="shortName" />
            </div>
          </div>
          <div class="form-group">
            <label for="scope" class="col-xs-2 control-label">生产企业</label>
            <div class="col-xs-3">
              <input type="text" class="form-control" id="factoryName" name="factoryName" />
            </div>
            <label for="scope" class="col-xs-2 control-label">库存预警</label>
            <div class="col-xs-3">
              <select class="form-control" id="warningStatus" name="warningStatus" >
                  <option value="">全部</option>
                  <option value="1">缺货</option>
                  <option value="2">库存预警</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label for="scope" class="col-xs-2 control-label">更新时间</label>
            <div class="col-xs-3">
              <div class="input-group input-large">
                <input type="text" id="beginUpdateTime" name="beginUpdateTime" class="form-control Wdate border-right-none" onclick="WdatePicker()" />
                <span class="input-group-addon">至</span>
                <input type="text" id="endUpdateIime" name="endUpdateIime" class="form-control Wdate border-left-none" onclick="WdatePicker()" />
              </div>
            </div>
            <div class="col-xs-5 text-right">
              <input type="button" class="btn btn-info" value="搜索">
            </div>
          </div>
        </div>
        </form>
      </div>
      <div class="padding-t-20">
        <input type="button"  class="btn btn-info" value="EXCEL更新库存"  onclick="showExcel()"/>
        <span class="color999">&nbsp;<i class="fa red fa-warning"></i>&nbsp;此符号表示当前库存已达到预警状态</span>
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
                    <col style="width: 10%;" />
                    <col style="width: 10%;" />
                    <col style="width: 10%;" />
                    <col style="width: 10%;" />
                    <col style="width: 5%;" />
                    <col style="width: 10%;" />
                    <col style="width: 10%;" />
                    <col style="width: 5%;" />
                  </colgroup>
                  <thead>
                  <tr>
                    <th>本公司产品编码</th>
                    <th>通用名</th>
                    <th>规格</th>
                    <th>生产企业</th>
                    <th>当前库存</th>
                    <th>冻结库存</th>
                    <th>前端可售库存</th>
                    <th>是否缺货</th>
                    <th>预警库存</th>
                    <th>更新时间</th>
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
<div class="modal fade" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" style="width:550px;">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">编辑</h4>
      </div>
      <div class="modal-body">
        <div class="form-horizontal">
          <div class="form-group">
            <label class="col-xs-3 control-label">产品编码</label>
            <div class="col-xs-7 control-label text-left" id="productCode">
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">当前库存</label>
            <div class="col-xs-7">
              <input type="hidden" class="form-control"  id="inventoryCode"/>
              <input type="hidden" class="form-control"  id="productName"/>
              <input type="text" class="form-control"  id="currentInventory"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">预警库存</label>
            <div class="col-xs-7">
              <input type="text" class="form-control"  id="warningInventory"/>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <input type="button" class="btn btn-danger" onclick="update()" value="修改">
        <input type="button" class="btn btn-default" data-dismiss="modal" value="拒绝">
      </div>
    </div>
  </div>
</div>
<%--框架右侧内容 end--%>

<%--EXCEL更新库存--%>
<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" style="width:650px;">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabe2">EXCEL更新库存</h4>
      </div>

      <div class="wrapper">
          <div class="row choseuser">
            <div class="padding-20 excel_d">
              <p class="top_title">关于EXCEL更新库存：</p>
              <p class="color999">尊敬的客户，EXCEL更新库存是为了快速的更新库存，您只需按步骤就可以更新库存</p>
              <p class="red padding-b-10">请注意：审核通过的商品才能维护库存。</p>
              <div><span>①</span><b class="margin-lr-20">下载模板</b>
                <a class="btn btn-info" href="${ctx}/static/include/excel/stockExcel.xls">下载模板</a>
                <p class="color999 padding-t-10">若是渠道掌控商品则填写。不填写默认非控销品种。最小拆零包装不填写默认！</p>
                <p class="color999 red">请注意，红色的为必填项</p>
              </div>
              <form method="post" id="form1" enctype="multipart/form-data">
              <div><span>②</span>
                <b class="margin-lr-20">上传填写完的文件</b>
                <input type="file" id="excelFile" name="excelFile" style="display:none;" onchange="fileType(this)">
                <button type="button" class="btn btn-info" onclick="excelFile.click()">上传文件</button>
                <i>商品列表.excel</i>
              </div>
              </form>
            </div>
          </div>
          <div class="row btn_box">
            <input type="button" class="btn btn-danger margin-r-10 tjbtn" value="提交"  onclick="importExcel()"/>
          </div>
        </div>
      </div>

    </div>
  </div>
</div>

<%--上传完成后显示结果 />--%>
<div class="modal fade" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content" style="width:600px;">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabe3">提交</h4>
      </div>
      <div class="modal-body">
        <div class="form-horizontal">
          <div class="form-group">
            <div class="col-xs-12 font-size-14" id="msgDiv">

            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">知道了</button>
      </div>
    </div>
  </div>
</div>


</body>

<%@ include file="../common_footer.jsp" %>
<script type="text/javascript" src="${ctx }/static/js/productInventory/product_inventory_manage.js"></script>
