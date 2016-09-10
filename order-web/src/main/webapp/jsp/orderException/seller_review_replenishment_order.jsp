<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>审核订单</title>
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
                    <li><a href="${ctx}/order/sellerOrderManage"><i class="fa fa-map-marker fa-3"></i>销售订单管理</a></li>
                    <li><a href="${ctx}/orderException/sellerReplenishmentOrderManage"><i class="fa fa-map-marker fa-3"></i>补货订单管理</a></li>
                    <li class="active">审核订单</li>
                </ol>
            </div>
            <div class="border-gray">
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label class="col-xs-2 control-label">补货订单号</label>
                        <div class="col-xs-2 control-label text-left">${orderExceptionDto.exceptionOrderId}</div>
                        <label class="col-xs-2 control-label">订单状态</label>
                        <div class="col-xs-2 control-label text-left"><span class="red margin-r-10">${orderExceptionDto.orderStatusName}</span> </div>
                        <label class="col-xs-2 control-label">原订单号</label>
                        <div class="col-xs-2 control-label text-left"><a href="${ctx}/order/getSupplyOrderDetails?flowId=${orderExceptionDto.flowId}">${orderExceptionDto.flowId}</a></div>
                    </div>
                </div>
            </div>
            <div class="row choseuser margin-t-20 border-gray">
                <div class="form-horizontal padding-t-26">
                    <input type="hidden" value="${orderExceptionDto.exceptionId}" id="exceptionId"/>
                    <div class="form-group">
                        <label class="col-xs-2 control-label">买家申请补货说明:</label>
                        <div class="col-xs-9 control-label text-left">${orderExceptionDto.returnDesc}</div>
                    </div>
                    <div class="form-group">
                        <label  class="col-xs-2 control-label">审核说明：</label>
                        <div class="col-xs-9">
                            <textarea class="form-control" rows="5" id="remark" maxlength="50"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-2"></label>
                        <div class="col-xs-1"><button type="button" class="btn btn-danger" onclick="review(2)">通过</button></div>
                        <div class="col-xs-1"><button type="button" class="btn btn-danger" onclick="review(3)">不通过</button></div>
                    </div>
                </div>
            </div>

            <div class="row choseuser margin-t-20 border-gray">
                <h2 class="row">订单信息</h2>
                <div class="modify">
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label class="col-xs-12 color999 padding-l-40 font-size-16">收货人信息</label>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">收货人</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.receivePerson}</div>
                            <label for="scope" class="col-xs-2 control-label">收货地址</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.receiveAddress}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">联系方式</label>
                            <div class="col-xs-10 control-label text-left">${orderExceptionDto.orderDelivery.receiveContactPhone}</div>
                        </div>
                    </div>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label class="col-xs-12 color999 padding-l-40 font-size-16">发货人信息</label>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">供应商</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.supplyName}</div>
                            <label for="scope" class="col-xs-2 control-label">发货人</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.deliveryPerson}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">联系方式</label>
                            <div class="col-xs-10 control-label text-left">${orderExceptionDto.orderDelivery.deliveryContactPhone}</div>
                        </div>
                    </div>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label class="col-xs-12 color999 padding-l-40 font-size-16">其他信息</label>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">支付方式</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.payTypeName}</div>
                            <label for="scope" class="col-xs-2 control-label">发票信息</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.billTypeName}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">下单时间</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderCreateTime}</div>
                        </div>

                    </div>
                </div>
            </div>

            <div class="row choseuser margin-t-20 border-gray">
                <h2 class="row">补货商品清单</h2>
                <div class="modify padding-20">
                    <table class="table table-box">
                        <colgroup>
                            <col style="width: 40%;" />
                            <col style="width: 15%;" />
                            <col style="width: 15%;" />
                            <col style="width: 15%;" />
                            <col style="width: 15%;" />
                        </colgroup>
                        <thead>
                        <tr>
                            <th>商品</th>
                            <th>批号</th>
                            <th>单价</th>
                            <th>数量</th>
                            <th>金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${orderExceptionDto != null && fn:length(orderExceptionDto.orderReturnList) gt 0 }">
                                <c:forEach var="orderReturnDto" items="${orderExceptionDto.orderReturnList}">
                                    <tr>
                                        <td>
                                            <div class="clearfix">
                                                <div class="fl">
                                                    <img  class="productImageUrl" spuCode="${orderReturnDto.spuCode}" alt="${orderReturnDto.productName}"  onerror="this.error = null;this.src='${STATIC_URL}/static/images/img_03.jpg'">
                                                </div>
                                                <div class="fl fontbox">
                                                    <p class="title">${orderReturnDto.productName}</p>
                                                    <p class="text">${orderReturnDto.manufactures}</p>
                                                    <p class="text">${orderReturnDto.specification}</p>
                                                </div>
                                            </div>
                                        </td>
                                        <td>${orderReturnDto.batchNumber}</td>
                                        <td>¥ <fmt:formatNumber value="${orderReturnDto.productPrice}" minFractionDigits="2"/></td>
                                        <td>x ${orderReturnDto.returnCount}</td>
                                        <td>¥ <fmt:formatNumber value="${orderReturnDto.returnPay}" minFractionDigits="2"/></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6">
                                        暂无补货商品信息
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>

                    <div class="text-right">
                        <p>商品金额： <fmt:formatNumber value="${orderExceptionDto.productPriceCount}" minFractionDigits="2"/>元<p>
                        <p class="red">订单金额：<fmt:formatNumber value="${orderExceptionDto.orderMoney}" minFractionDigits="2"/>元<p>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

</div>
<div class="footer">
    <p>监管机构：国家食品药品监督管理局   广东省食品药品监督管理局   广州市食品药品监督管理局</p>
    <p>Copyright (C) 2010-2015 本网站版权归广东壹号药业有限公司所有  网站备案号:粤ICP备15048678</p>
</div>
<!--框架右侧内容 end-->
</div>
</body>
<script type="text/javascript" src="${STATIC_URL}/static/js/jquery-1.12.1.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/b_common.js"></script>
<script type="text/javascript" src="http://yhycstatic.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/common.js"></script>
<script type="text/javascript">
    function review(type){
        if(type == 2 || type == 3){
            var exceptionId = $("#exceptionId").val();
            if(exceptionId == ''){
                alertModal("订单编号错误");
                return;
            }
            var remark = $("#remark").val().trim();
            var data = {exceptionId:exceptionId,remark:remark,orderStatus:type};

            $.ajax({
                url: ctx+"/orderException/sellerReviewReplenishmentOrder",
                data: JSON.stringify(data),
                type: 'POST',
                contentType: "application/json;charset=UTF-8",
                success: function (data) {
                    if (data.statusCode || data.message) {
                        alertModal(data.message);
                        return;
                    }
                    alertModal("操作成功");
                    setTimeout(function(){
                        window.location.href=ctx+"/orderException/sellerReplenishmentOrderManage";
                    },1000)
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alertModal("审核失败");
                }
            });
        }
    }
</script>
</html>


