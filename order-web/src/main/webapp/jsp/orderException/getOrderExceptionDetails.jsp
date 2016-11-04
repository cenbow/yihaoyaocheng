
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/10
  Time: 11:00
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
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
            <div class="qy_basenews">
                <div class="row no-margin">
                    <ol class="breadcrumb">
                        <c:if test="${orderExceptionDto.userType==1}">
                            <li><a href="#"><i class="fa fa-map-marker fa-3"></i>首页</a></li>
                            <li><a href="${ctx}/orderException/buyerRejectOrderManage"><i class="fa fa-map-marker fa-3"></i>拒收订单管理</a></li>
                        </c:if>
                        <c:if test="${orderExceptionDto.userType==2}">
                            <li><a href="#"><i class="fa fa-map-marker fa-3"></i>首页</a></li>
                            <li><a href="${ctx}/orderException/sellerRejcetOrderManage"><i class="fa fa-map-marker fa-3"></i>拒收订单管理</a></li>
                        </c:if>
                        <li class="active">订单详情</li>
                    </ol>
                </div>
                <div class="border-gray">
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">拒收订单号</label>
                            <div class="col-xs-2 control-label text-left">${orderExceptionDto.exceptionOrderId}</div>
                            <label for="scope" class="col-xs-2 control-label">订单状态</label>
                            <div class="col-xs-2 control-label text-left"><span class="red margin-r-10">${orderExceptionDto.orderStatusName}</span> </div>
                            <label for="scope" class="col-xs-2 control-label">关联订单号</label>
                            <div class="col-xs-2 control-label text-left">
                                <c:if test="${orderExceptionDto.userType==1}">
                                    <a href="${ctx}/order/getBuyOrderDetails?flowId=${orderExceptionDto.flowId}">${orderExceptionDto.flowId}</a>
                                </c:if>
                                <c:if test="${orderExceptionDto.userType==2}">
                                    <a href="${ctx}/order/getSupplyOrderDetails?flowId=${orderExceptionDto.flowId}">${orderExceptionDto.flowId}</a>
                                </c:if>
                            </div>
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
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.createTime}</div>
                                <label for="scope" class="col-xs-2 control-label">拒收说明</label>
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.returnDesc}</div>
                            </div>

                            <div class="form-group">
                                <label for="scope" class="col-xs-2 control-label">卖家确认/关闭时间</label>
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.reviewTime}</div>
                                <label for="scope" class="col-xs-2 control-label">确认/关闭说明</label>
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.remark}</div>
                            </div>

                        </div>
                    </div>
                </div>

                <div class="row choseuser margin-t-20 border-gray">
                    <h2 class="row">商品清单</h2>
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
                                                    <td>¥ <fmt:formatNumber value="${orderReturnDto.returnPay}" minFractionDigits="2"/></td>
                                                </tr>
                                            </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="6">
                                                    暂无拒收商品信息
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>

                        <div class="text-right">
                            <p>商品金额： <fmt:formatNumber value="${orderExceptionDto.productPriceCount}" minFractionDigits="2"/>元<p>
                            <p class="red">订单金额：<fmt:formatNumber value="${orderExceptionDto.productPriceCount}" minFractionDigits="2"/>元<p>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

</div>
</div>
</body>
<%@ include file="../common_footer.jsp" %>
</html>


