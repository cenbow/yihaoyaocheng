<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单详情</title>
    <script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/sidebar.js"></script>
    <link href="http://yhycstatic.yaoex.com/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="http://yhycstatic.yaoex.com/css/font-awesome.css" type="text/css" rel="stylesheet" />
    <%@ include file="../config.jsp"%>
    <link href="${STATIC_URL}/static/css/common.css" rel="stylesheet" />

</head>
<body>
<!--框架右侧内容 start-->
<div id="main-content" class="main-content">
    <div class="wrapper">
        <div class="qy_basenews">
            <div class="row no-margin">
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>订单管理</a></li>
                    <li class="active">订单详情</li>
                </ol>
            </div>
            <div class="border-gray">
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">订单号</label>

                        <div class="col-xs-2 control-label text-left">${orderDetailsDto.flowId}</div>
                        <label for="scope" class="col-xs-2 control-label">订单状态 </label>

                        <div class="col-xs-6 control-label text-left"><span
                                class="red margin-r-10">${orderDetailsDto.orderStatusName}</span></div>
                </div>
            </div>
        </div>
            <div class="row margin-t-20">
                <div class="form-horizontal padding-t-26">
                    <form action="${ctx}/order/addForConfirmMoney" method="post" id="confirmForm" >
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">订单金额</label>
                            <div class="col-xs-9 control-label text-left" >${orderDetailsDto.orgTotal}元</div>
                            <input type="hidden" id="orgTotalMoney"  value="${orderDetailsDto.orgTotal}"/>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">实际收款金额</label>
                            <div class="col-xs-2">
                                <input class="form-control" type="text" id="refunSettlementMoney" oninput="changediffMoney();" name="refunSettlementMoney" />
                            </div>
                            <div class="col-xs-8 control-label text-left">元</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">应收实收差异</label>
                            <div class="col-xs-9 control-label text-left"><div  id="diffMoney" style="float:left"></div>元</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">备注</label>
                            <div class="col-xs-9">
                                <textarea class="form-control" rows="5" id="remark" name="remark"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2"></label>
                            <div class="col-xs-9"><button type="button" class="btn btn-danger" onclick="submitForm();">确认收款</button></div>
                        </div>
                        <input type="hidden" id="orderId" name="orderId" value="${orderDetailsDto.orderId}"/>
                    </form>
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

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.orderDelivery.receivePerson}</div>
                        <label for="scope" class="col-xs-2 control-label">收货地址</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.orderDelivery.receiveAddress}</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">联系方式</label>

                        <div class="col-xs-10 control-label text-left">${orderDetailsDto.orderDelivery.receiveContactPhone}</div>
                    </div>
                </div>
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label class="col-xs-12 color999 padding-l-40 font-size-16">发货人信息</label>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">供应商</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.supplyName}</div>
                        <label for="scope" class="col-xs-2 control-label">发货人</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.orderDelivery.deliveryPerson}</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">联系方式</label>

                        <div class="col-xs-10 control-label text-left">${orderDetailsDto.orderDelivery.deliveryContactPhone}</div>
                    </div>
                </div>
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label class="col-xs-12 color999 padding-l-40 font-size-16">其他信息</label>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">支付方式</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.payTypeName}</div>
                        <label for="scope" class="col-xs-2 control-label">发票信息:</label>
                        <div class="col-xs-3 control-label text-left">
                            <c:if test="${orderDetailsDto.billType==1}">增值税专用发票</c:if>
                            <c:if test="${orderDetailsDto.billType==2}">增值税普通发票</c:if>
                        </div>

                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">下单时间</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.createTime}</div>
                        <label for="scope" class="col-xs-2 control-label">买家留言</label>

                        <div class="col-xs-8 control-label text-left">${orderDetailsDto.leaveMessage}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row choseuser margin-t-20 border-gray">
            <div class="row choseuser margin-t-20 border-gray">
                <h2 class="row">商品清单</h2>

                <div class="modify padding-20">
                    <table class="table table-box">
                        <colgroup>
                            <col style="width: 30%;"/>
                            <col style="width: 15%;"/>
                            <col style="width: 15%;"/>
                            <col style="width: 15%;"/>
                            <col style="width: 15%;"/>
                            <col style="width: 10%;"/>
                        </colgroup>
                        <thead>
                        <tr>
                            <th>商品</th>
                            <th>单价</th>
                            <th>数量</th>
                            <th>金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%--遍历该供应商的商品信息  开始--%>
                        <c:choose>
                            <c:when test="${orderDetailsDto != null && fn:length(orderDetailsDto.details) gt 0 }">
                                <c:forEach var="details" items="${orderDetailsDto.details}" varStatus="detailsVarStatus">
                                    <input type="hidden" name="productId" value="${details.orderDetailId}"/>
                                    <%--  <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].id" id="${shoppingCartDto.productId}" value="${shoppingCartDto.productId}"/>
                                      <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productCount" value="${shoppingCartDto.productCount}"/>
                                      <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productPrice" value="${shoppingCartDto.productPrice}"/>--%>
                                    <tr>
                                        <td>
                                            <div class="clearfix">
                                                <div class="fl">
                                                    <img alt="${details.shortName}" class="productImageUrl" spuCode="${details.spuCode}"  onerror="this.error = null;this.src='${STATIC_URL}/static/images/img_03.jpg'">
                                                </div>
                                                <div class="fl fontbox">
                                                    <p class="title">${details.shortName}</p>

                                                    <p class="text">${details.manufactures}</p>

                                                    <p class="text">${details.specification}</p>
                                                </div>
                                            </div>
                                        </td>
                                        <td>￥ <fmt:formatNumber value="${details.productPrice}" minFractionDigits="2"/></td>
                                        <td>x${details.productCount}</td>
                                        <td>￥  <fmt:formatNumber value="${details.productPrice * details.productCount}" minFractionDigits="2"/> </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                        <%--遍历该供应商的商品信息  结束--%>
                        </tbody>
                    </table>
                    <div class="text-right">
                        <p>商品金额：￥ <fmt:formatNumber value="${orderDetailsDto.productTotal}" minFractionDigits="2"/>元

                        <p>


                        <p>优惠券：- 0.00元

                        <p>

                        <p class="red">订单金额：￥<fmt:formatNumber value="${orderDetailsDto.orgTotal}" minFractionDigits="2"/>元

                        <p>

                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="modal fade" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:650px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">收款详情</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">订单金额</label>

                        <div class="col-xs-5 control-label text-left">${orderDetailsDto.orgTotal}元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">实际收款金额</label>

                        <div class="col-xs-5 control-label text-left">${orderDetailsDto.finalPay} 元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">应收实收差异</label>

                        <div class="col-xs-5 control-label text-left">${orderDetailsDto.orgTotal-orderDetailsDto.finalPay}元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">备注</label>

                        <div class="col-xs-5 control-label text-left">${orderDetailsDto.remark}</div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${STATIC_URL}/static/js/jquery-1.12.1.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/b_common.js"></script>
</html>
<script type="text/javascript" src="http://yhycstatic.yaoex.com/jsp/common/footer.js"></script>
<script type="text/javascript" src="http://yhycstatic.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/order/confirmMoney.js"></script>
<script>
    $(".gathering").click(function(){
        $("#myModal1").modal();
    });
    $(".lookgoodlist").click(function(){
        $("#myModal2").modal();
    });
    $("#delete").click(function(){
        alertModal("确定要删除吗？");
    });
</script>