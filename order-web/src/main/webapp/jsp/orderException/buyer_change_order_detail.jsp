<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单详情</title>
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
        <c class="qy_basenews">
            <div class="row no-margin">
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>首页</a></li>
                    <li><a href="${ctx}/orderException/buyerChangeGoodsOrderManage"><i class="fa fa-map-marker fa-3"></i>换货订单管理</a></li>
                    <li class="active">订单详情</li>
                </ol>
            </div>
            <div class="border-gray">
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label class="col-xs-2 control-label">换货订单号</label>
                        <div class="col-xs-2 control-label text-left">${orderExceptionDto.exceptionOrderId}</div>
                        <label class="col-xs-2 control-label">订单状态</label>
                        <div class="col-xs-2 control-label text-left"><span class="red margin-r-10">${orderExceptionDto.orderStatusName}</span> </div>
                        <label class="col-xs-2 control-label">原订单号</label>
                        <div class="col-xs-2 control-label text-left"><a href="${ctx}/order/getBuyOrderDetails?flowId=${orderExceptionDto.flowId}" class="undeline">${orderExceptionDto.flowId}</a></div>
                    </div>
                </div>
            </div>

            <div class="row choseuser margin-t-20 border-gray">
                <h2 class="row">订单信息</h2>
                <div class="modify">
                    <%-- <div class="form-horizontal padding-t-26">
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
                    </div> --%>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label class="col-xs-12 color999 padding-l-40 font-size-16"></label>
                        </div>
                        <div class="form-group">
	                        <label for="scope" class="col-xs-2 control-label">供应商</label>
	
	                        <div class="col-xs-3 control-label text-left">${orderExceptionDto.supplyName}</div>
	                        <label for="scope" class="col-xs-2 control-label"></label>
	                        <div class="col-xs-3 control-label text-left"></div>
                       </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">关联订单支付方式</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.payTypeName}</div>
                            <label for="scope" class="col-xs-2 control-label">发票信息</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.billTypeName}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">申请换货时间</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderCreateTime}</div>
                            <label for="scope" class="col-xs-2 control-label">换货说明</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.returnDesc}</div>
                        </div>

                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">卖家确认/关闭时间</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.reviewTime}</div>
                            <label for="scope" class="col-xs-2 control-label">卖家确认/关闭说明</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.remark}</div>
                        </div>

                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">买家发货时间</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.buyerDeliverTime}</div>
                            <label for="scope" class="col-xs-2 control-label">卖家确认收货时间</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.sellerReceiveTime}</div>
                        </div>

                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">卖家发货时间</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.deliverTime}</div>
                            <label for="scope" class="col-xs-2 control-label">买家确认收货时间</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.receiveTime}</div>
                        </div>

                    </div>
                </div>
            </div>
            
                 <!-- 买卖方换货发货地址 -->
               <c:if test="${orderExceptionDto.orderDeliveryList != null && fn:length(orderExceptionDto.orderDeliveryList) gt 0 }">
                <c:forEach items="${orderExceptionDto.orderDeliveryList}" var="item" varStatus="status">
				      <c:if test="${status.index == 0}">
					   <div class="row choseuser margin-t-20 border-gray">
			                <h2 class="row"> 买家换货发货地址</h2>
	                        <div class="form-horizontal padding-t-26">
	                                <div class="form-group">
	                                    <label for="scope" class="col-xs-2 control-label">发货地址</label>
	                                    <div class="col-xs-3 control-label text-left">${item.deliveryAddress}</div>
	                                    <label for="scope" class="col-xs-2 control-label">联系人</label>
	                                    <div class="col-xs-3 control-label text-left">${item.deliveryPerson}</div>
	                                </div>
	                                <div class="form-group">
	                                    <label for="scope" class="col-xs-2 control-label">联系人电话</label>
	                                    <div class="col-xs-3 control-label text-left">${item.deliveryContactPhone}</div>
	                                    <label for="scope" class="col-xs-2 control-label"></label>
	                                    <div class="col-xs-3 control-label text-left"></div>
	                                </div>
	                        </div>
                       </div>
					       <div class="row choseuser margin-t-20 border-gray">
		                        <h2 class="row">卖家换货收货地址</h2>
		                        <div class="form-horizontal padding-t-26">
		                                <div class="form-group">
		                                    <label for="scope" class="col-xs-2 control-label">收货地址</label>
		                                    <div class="col-xs-3 control-label text-left">${item.receiveAddress}</div>
		                                    <label for="scope" class="col-xs-2 control-label">联系人</label>
		                                    <div class="col-xs-3 control-label text-left">${item.receivePerson}</div>
		                                </div>
		                                <div class="form-group">
		                                    <label for="scope" class="col-xs-2 control-label">联系人电话</label>
		                                    <div class="col-xs-3 control-label text-left">${item.receiveContactPhone}</div>
		                                    <label for="scope" class="col-xs-2 control-label"></label>
		                                    <div class="col-xs-3 control-label text-left"></div>
		                                </div>
		                        </div>
		                    </div>
					  </c:if>
					  <c:if test="${status.index ==1}">
					        <div class="row choseuser margin-t-20 border-gray">
			                <h2 class="row"> 卖家换货发货地址</h2>
	                        <div class="form-horizontal padding-t-26">
	                                <div class="form-group">
	                                    <label for="scope" class="col-xs-2 control-label">发货地址</label>
	                                    <div class="col-xs-3 control-label text-left">${item.deliveryAddress}</div>
	                                    <label for="scope" class="col-xs-2 control-label">联系人</label>
	                                    <div class="col-xs-3 control-label text-left">${item.deliveryPerson}</div>
	                                </div>
	                                <div class="form-group">
	                                    <label for="scope" class="col-xs-2 control-label">联系人电话</label>
	                                    <div class="col-xs-3 control-label text-left">${item.deliveryContactPhone}</div>
	                                    <label for="scope" class="col-xs-2 control-label"></label>
	                                    <div class="col-xs-3 control-label text-left"></div>
	                                </div>
	                        </div>
                       </div>
					       <div class="row choseuser margin-t-20 border-gray">
		                        <h2 class="row">买家换货收货地址</h2>
		                        <div class="form-horizontal padding-t-26">
		                                <div class="form-group">
		                                    <label for="scope" class="col-xs-2 control-label">收货地址</label>
		                                    <div class="col-xs-3 control-label text-left">${item.receiveAddress}</div>
		                                    <label for="scope" class="col-xs-2 control-label">联系人</label>
		                                    <div class="col-xs-3 control-label text-left">${item.receivePerson}</div>
		                                </div>
		                                <div class="form-group">
		                                    <label for="scope" class="col-xs-2 control-label">联系人电话</label>
		                                    <div class="col-xs-3 control-label text-left">${item.receiveContactPhone}</div>
		                                    <label for="scope" class="col-xs-2 control-label"></label>
		                                    <div class="col-xs-3 control-label text-left"></div>
		                                </div>
		                        </div>
		                    </div>
					  </c:if>
                </c:forEach>
            </c:if>

            <c:choose>
                <c:when test="${orderExceptionDto != null && fn:length(orderExceptionDto.orderDeliveryList) gt 0 }">
                    <c:forEach var="orderDelivery" items="${orderExceptionDto.orderDeliveryList}" varStatus="orderDeliveryStatus">
                    <div class="row choseuser margin-t-20 border-gray">
                        <h2 class="row">
                            <c:if test="${orderDeliveryStatus.index == 0}">买家发货配送信息</c:if>
                            <c:if test="${orderDeliveryStatus.index == 1}">卖家发货配送信息</c:if>
                        </h2>
                        <div class="form-horizontal padding-t-26">
                            <c:if test="${orderDelivery.deliveryMethod==1}">
                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">配送方式</label>
                                    <div class="col-xs-3 control-label text-left">自有物流</div>
                                    <label for="scope" class="col-xs-2 control-label">预计送达时间</label>
                                    <div class="col-xs-3 control-label text-left">${orderDelivery.deliveryDate}</div>
                                </div>
                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">联系人</label>
                                    <div class="col-xs-3 control-label text-left">${orderDelivery.deliveryContactPerson}</div>
                                    <label for="scope" class="col-xs-2 control-label">联系人电话</label>
                                    <div class="col-xs-3 control-label text-left">${orderDelivery.deliveryExpressNo}</div>
                                </div>
                            </c:if>

                            <c:if test="${orderDelivery.deliveryMethod==2}">
                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">配送方式</label>
                                    <div class="col-xs-3 control-label text-left">第三方物流</div>
                                    <label for="scope" class="col-xs-2 control-label">物流公司</label>
                                    <div class="col-xs-3 control-label text-left">${orderDelivery.deliveryContactPerson}</div>
                                </div>

                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">物流单号</label>
                                    <div class="col-xs-3 control-label text-left">${orderDelivery.deliveryExpressNo}</div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>


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
                    </colgroup>
                    <thead>
                    <tr>
                        <th>商品</th>
                        <th>批次</th>
                        <th>单价</th>
                        <th>数量</th>
                        <th>金额</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%--遍历该供应商的商品信息  开始--%>
                    <input type="hidden" id="flowId" name="flowId" value="${orderExceptionDto.exceptionOrderId}"/>
                    <input type="hidden" id="userType" name="userType" value="1"/>
                    <c:choose>
                        <c:when test="${orderExceptionDto != null && fn:length(orderExceptionDto.orderReturnList) gt 0 }">
                            <c:forEach var="orderReturnDto" items="${orderExceptionDto.orderReturnList}" varStatus="detailsVarStatus">
                                <tr>
                                    <td>
                                        <div class="clearfix">
                                            <div class="fl">
                                                <a href='http://mall.yaoex.com/product/productDetail/${orderReturnDto.spuCode}/${orderReturnDto.supplyId}'>
                                                <img  class="productImageUrl" spuCode="${orderReturnDto.spuCode}" alt="${orderReturnDto.shortName}"  onerror="this.error = null;this.src='${STATIC_URL}/static/images/img_03.jpg'">
                                           		</a>
                                            </div>
                                            <div class="fl fontbox">
                                                <p class="title"><a href='http://mall.yaoex.com/product/productDetail/${orderReturnDto.spuCode}/${orderReturnDto.supplyId}'>
                                                   ${orderReturnDto.shortName}</a></p>
                                                <p class="text">${orderReturnDto.manufactures}</p>
                                                <p class="text">${orderReturnDto.specification}</p>
                                            </div>
                                        </div>
                                    </td>
                                    <td>${orderReturnDto.batchNumber}</td>
                                    <td>¥ <fmt:formatNumber value="${orderReturnDto.productPrice}" minFractionDigits="2"/></td>
                                    <td>x ${orderReturnDto.returnCount}</td>
                                    <td>¥ <fmt:formatNumber value="${orderReturnDto.productAllMoney}" minFractionDigits="2"/></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6">
                                    暂无商品信息
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <%--遍历该供应商的商品信息  结束--%>
                    </tbody>
                </table>
               <%--  <c:if test="${orderExceptionDto.orderStatus==8 || orderExceptionDto.orderStatus==9}"> --%>
                     <div><a class="undeline" onclick="listReplenishment()">已换货商品清单</a></div>
                <%-- </c:if> --%>
                <div class="text-right">
                    <p>商品金额：${orderExceptionDto.productPriceCount}元<p>
                    <p>满减金额： -<fmt:formatNumber value="${orderExceptionDto.orderShareMoney}" minFractionDigits="2"/>元<p>
                    <p class="red">订单金额：￥${orderExceptionDto.orderPriceCount}元<p>
                </div>
            </div>

        </div>
    </div>
</div>

    <div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="width: 1000px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">收货商品清单</h4>
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
	                        <col style="width: 8%;">
	                        <col style="width: 8%;">
	                        <col style="width: 8%;">
	                        <col style="width: 8%;">
	                        <col style="width: 10%;">
	                        <col style="width: 10%;">
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
                    <div class="pager" id="J_pager"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

</div>
</div>
</body>
<%@ include file="../common_footer.jsp" %>
<script type="text/javascript" src="${ctx }/static/js/order/order_delivery_detail.js"></script>
<script>
    $(".lookgoodlist").click(function(){
        $("#myModal").modal();
    });
    $("#delete").click(function(){
        alertModal("确定要删除吗？");
    });
</script>
</html>


