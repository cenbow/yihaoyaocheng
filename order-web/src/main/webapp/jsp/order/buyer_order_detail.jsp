<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单详情</title>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/sidebar.js"></script>
    <link href="http://static.yaoex.com/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="http://static.yaoex.com/css/font-awesome.css" type="text/css" rel="stylesheet" />
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

                        <div class="col-xs-6 control-label text-left"><span class="red margin-r-10">${orderDetailsDto.orderStatusName}</span>
                            <c:if test="${orderDetailsDto.orderStatus==9}">
                            <a class="undeline" href="${ctx}/orderException/getDetails-1/${orderDetailsDto.flowId}" >查看拒收订单</a>
                            </c:if>
                            <c:if test="${orderDetailsDto.orderStatus==10}">
                            <a href="${ctx}/orderException/getReplenishmentDetails-1/${orderDetailsDto.flowId}" class="undeline">查看补货订单</a>
                            </c:if>
                        </div>
                    </div>
            </div>
            <div class="container-fluid progress">

            <c:if test="${orderDetailsDto.payType!=2&&orderDetailsDto.orderStatus==1}">
                <div class="row progress_bar">
                    <div class="col-xs-3 cur"><span>1</span></div>
                    <div class="col-xs-3"><span>2</span></div>
                    <div class="col-xs-3"><span>3</span></div>
                    <div class="col-xs-3"><span>4</span></div>
                </div>
                <div class="row progress_num">
                    <div class="col-xs-3">待付款</div>
                    <div class="col-xs-3">待发货</div>
                    <div class="col-xs-3">待收货</div>
                    <div class="col-xs-3">已完成</div>
                </div>
            </c:if>
                <c:if test="${orderDetailsDto.orderStatus==5}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3 cur"><span>2</span></div>
                        <div class="col-xs-3"><span>3</span></div>
                        <div class="col-xs-3"><span>4</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待付款</div>
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">已完成</div>
                    </div>
                </c:if>

                <c:if test="${orderDetailsDto.orderStatus==6||orderDetailsDto.orderStatus==12}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3 cur"><span>2</span></div>
                        <div class="col-xs-3 cur"><span>3</span></div>
                        <div class="col-xs-3"><span>4</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待付款</div>
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">已完成</div>
                    </div>
                </c:if>
                <c:if test="${orderDetailsDto.orderStatus==8||orderDetailsDto.orderStatus==13||orderDetailsDto.orderStatus==11||orderDetailsDto.orderStatus==14}">
                    <div class="row progress_bar">
                        if
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3 cur"><span>2</span></div>
                        <div class="col-xs-3 cur"><span>3</span></div>
                        <div class="col-xs-3 cur"><span>4</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待付款</div>
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">已完成</div>
                    </div>
                </c:if>
                <c:if test="${orderDetailsDto.payType!=2&&(orderDetailsDto.orderStatus==2||orderDetailsDto.orderStatus==3||orderDetailsDto.orderStatus==4||orderDetailsDto.orderStatus==7)}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3"><span>2</span></div>
                        <div class="col-xs-3"><span>3</span></div>
                        <div class="col-xs-3"><span>4</span></div>
                        <div class="col-xs-3 cur"><span>5</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待付款</div>
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">已完成</div>
                        <div class="col-xs-3">已取消</div>
                    </div>
                </c:if>
                <c:if test="${orderDetailsDto.payType!=2&&orderDetailsDto.orderStatus==9}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3 cur"><span>2</span></div>
                        <div class="col-xs-3 cur"><span>3</span></div>
                        <div class="col-xs-3 cur"><span>4</span></div>
                        <div class="col-xs-3 "><span>5</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待付款</div>
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">拒收中</div>
                        <div class="col-xs-3">已完成</div>
                    </div>
                </c:if>
                <c:if test="${orderDetailsDto.payType!=2&&orderDetailsDto.orderStatus==10}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3 cur"><span>2</span></div>
                        <div class="col-xs-3 cur"><span>3</span></div>
                        <div class="col-xs-3 cur"><span>4</span></div>
                        <div class="col-xs-3 "><span>5</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待付款</div>
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">补货中</div>
                        <div class="col-xs-3">已完成</div>
                    </div>
                </c:if>
                <c:if test="${orderDetailsDto.payType==2&&orderDetailsDto.orderStatus==1}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3"><span>2</span></div>
                        <div class="col-xs-3"><span>3</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">已完成</div>
                    </div>
                </c:if>
                <c:if test="${orderDetailsDto.payType==2&&(orderDetailsDto.orderStatus==2||orderDetailsDto.orderStatus==3||orderDetailsDto.orderStatus==4||orderDetailsDto.orderStatus==7)}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3"><span>2</span></div>
                        <div class="col-xs-3"><span>3</span></div>
                        <div class="col-xs-3 cur"><span>4</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">已完成</div>
                        <div class="col-xs-3">已取消</div>
                    </div>
                </c:if>
                <c:if test="${orderDetailsDto.payType==2&&orderDetailsDto.orderStatus==9}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3 cur"><span>2</span></div>
                        <div class="col-xs-3 cur"><span>3</span></div>
                        <div class="col-xs-3"><span>4</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">拒收中</div>
                        <div class="col-xs-3">已完成</div>
                    </div>
                </c:if>
                <c:if test="${orderDetailsDto.payType==2&&orderDetailsDto.orderStatus==10}">
                    <div class="row progress_bar">
                        <div class="col-xs-3 cur"><span>1</span></div>
                        <div class="col-xs-3 cur"><span>2</span></div>
                        <div class="col-xs-3 cur"><span>3</span></div>
                        <div class="col-xs-3"><span>4</span></div>
                    </div>
                    <div class="row progress_num">
                        <div class="col-xs-3">待发货</div>
                        <div class="col-xs-3">待收货</div>
                        <div class="col-xs-3">补货中</div>
                        <div class="col-xs-3">已完成</div>
                    </div>
                </c:if>
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
                        <label for="scope" class="col-xs-2 control-label">下单时间</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.createTime}</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">付款时间</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.payTime}</div>
                        <label for="scope" class="col-xs-2 control-label">收款确认时间</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.payTime}
                        <c:if test="${orderDetailsDto.payType==3}">
                            <a class="undeline margin-l-10 gathering">收款详情</a>
                        </c:if>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">发货时间</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.deliverTime}</div>
                        <label for="scope" class="col-xs-2 control-label">延期收货时间</label>

                        <div class="col-xs-3 control-label text-left">
                            <p>${orderDetailsDto.delayLog}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">确认收货时间</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.receiveTime}</div>
                        <label for="scope" class="col-xs-2 control-label">确认收货类型</label>

                        <div class="col-xs-3 control-label text-left">
                            <c:if test="${orderDetailsDto.receiveType!=null&&orderDetailsDto.receiveType == 1}">
                                买家确认收货
                            </c:if>
                            <c:if test="${orderDetailsDto.receiveType!=null&&orderDetailsDto.receiveType == 2}">
                                系统确认收货
                            </c:if>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">取消订单时间</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.cancelTime}</div>
                        <label for="scope" class="col-xs-2 control-label">取消原因</label>

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.cancelResult}</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">备注</label>

                        <div class="col-xs-8 control-label text-left">${orderDetailsDto.remark}</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">买家留言</label>

                        <div class="col-xs-8 control-label text-left">${orderDetailsDto.leaveMessage}</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row choseuser margin-t-20 border-gray">
            <div class="modify">
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label class="col-xs-12 color999 padding-l-40 font-size-16">配送信息</label>
                    </div>
                    <c:if test="${orderDetailsDto.orderDelivery.deliveryMethod==1}">
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">配送方式</label>
                            <div class="col-xs-3 control-label text-left">自有物流</div>
                            <label for="scope" class="col-xs-2 control-label">联系人</label>
                            <div class="col-xs-3 control-label text-left">${orderDetailsDto.orderDelivery.deliveryContactPerson}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">预计到达时间</label>
                            <div class="col-xs-3 control-label text-left">${orderDetailsDto.orderDelivery.deliveryDate}</div>
                            <label for="scope" class="col-xs-2 control-label">联系人电话</label>
                            <div class="col-xs-3 control-label text-left">${orderDetailsDto.orderDelivery.deliveryExpressNo}</div>
                        </div>
                    </c:if>
                    <c:if test="${orderDetailsDto.orderDelivery.deliveryMethod==2}">
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">配送方式</label>
                            <div class="col-xs-3 control-label text-left">第三方物流</div>
                            <label for="scope" class="col-xs-2 control-label">物流公司</label>
                            <div class="col-xs-3 control-label text-left">${orderDetailsDto.orderDelivery.deliveryContactPerson}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">物流单号</label>
                            <div class="col-xs-3 control-label text-left">${orderDetailsDto.orderDelivery.deliveryExpressNo}</div>
                        </div>
                    </c:if>
                </div>
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label class="col-xs-12 color999 padding-l-40 font-size-16">支付信息</label>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">支付方式:</label>
                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.payTypeName}</div>
                        <label for="scope" class="col-xs-2 control-label">支付状态</label>

                        <div class="col-xs-3 control-label text-left">
                            <c:if test="${orderDetailsDto.payStatus==1}">已支付</c:if>
                            <c:if test="${orderDetailsDto.payStatus==0}">未支付</c:if>
                        </div>
                    </div>
                </div>
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label class="col-xs-12 color999 padding-l-40 font-size-16">发票信息</label>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-2 control-label">发票信息:</label>
                        <div class="col-xs-3 control-label text-left">
                            <c:if test="${orderDetailsDto.billType==1}">增值税专用发票</c:if>
                            <c:if test="${orderDetailsDto.billType==2}">增值税普通发票</c:if>
                        </div>
                    </div>
                </div>
            </div>
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
                            <th>确认收货数量</th>
                            <th>确认收货金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%--遍历该供应商的商品信息  开始--%>
                        <input type="hidden" id="flowId" name="flowId" value="${orderDetailsDto.flowId}"/>
                        <input type="hidden" id="userType" name="userType" value="1"/>
                        <c:choose>
                            <c:when test="${orderDetailsDto != null && fn:length(orderDetailsDto.details) gt 0 }">
                                <c:forEach var="details" items="${orderDetailsDto.details}" varStatus="detailsVarStatus">
                                    <input type="hidden" name="productId" value="${details.orderDetailId}"/>
                                   <tr>
                                        <td>
                                            <div class="clearfix">
                                                <div class="fl">
                                                    <img src="" alt="${details.productName}"  onerror="this.error = null;this.src='${STATIC_URL}/static/images/img_03.jpg'">
                                                </div>
                                                <div class="fl fontbox">
                                                    <p class="title">${details.productName}</p>

                                                    <p class="text">${details.manufactures}</p>

                                                    <p class="text">${details.specification}</p>
                                                </div>
                                            </div>
                                        </td>
                                       <td>￥ <fmt:formatNumber value="${details.productPrice}" minFractionDigits="2"/></td>
                                       <td>x${details.productCount}</td>
                                       <td>￥  <fmt:formatNumber value="${details.productPrice * details.productCount}" minFractionDigits="2"/> </td>
                                       <td>x ${details.recieveCount}</td>
                                       <td>￥  <fmt:formatNumber value="${details.productPrice * details.recieveCount}" minFractionDigits="2"/></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                        <%--遍历该供应商的商品信息  结束--%>
                        </tbody>
                    </table>
                    <div>
                        <a class="undeline" onclick="listPg()">查看收货商品清单</a>
                    </div>
                    <div class="text-right">
                        <p>商品金额：￥ <fmt:formatNumber value="${orderDetailsDto.productTotal}" minFractionDigits="2"/>元

                        <p>

                        <p>确认收货商品金额：￥ <fmt:formatNumber value="${orderDetailsDto.receiveTotal}" minFractionDigits="2"/>元

                        <p>

                        <p>优惠券：- 00.00元

                        <p>

                        <p class="red">订单金额：￥<fmt:formatNumber value="${orderDetailsDto.orgTotal}" minFractionDigits="2"/>元

                        <p>

                        <p class="red">结算订单金额：￥<fmt:formatNumber value="${orderDetailsDto.receiveTotal}" minFractionDigits="2"/>元

                        <p>
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

                        <div class="col-xs-5 control-label text-left"><fmt:formatNumber value="${orderDetailsDto.orgTotal}" minFractionDigits="2"/>元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">实际收款金额</label>

                        <div class="col-xs-5 control-label text-left"><fmt:formatNumber value="${orderDetailsDto.finalPay}" minFractionDigits="2"/>元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">应收实收差异</label>

                        <div class="col-xs-5 control-label text-left"><fmt:formatNumber value="${orderDetailsDto.finalPay-orderDetailsDto.orgTotal}" minFractionDigits="2"/>元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">备注</label>

                        <div class="col-xs-5 control-label text-left">${orderDetailsDto.settlementRemark}</div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
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
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                        <col style="width: 10%;">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>订单行号</th>
                        <th>商品编码</th>
                        <th>批号</th>
                        <th>商品名</th>
                        <th>通用名</th>
                        <th>规格</th>
                        <th>剂型</th>
                        <th>生产企业</th>
                        <th>采购数量</th>
                        <th>收货数量</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <div class="pager" id="J_pager2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</div>
</body>
<script type="text/javascript" src="${STATIC_URL}/static/js/jquery-1.12.1.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/b_common.js"></script>
</html>
<script type="text/javascript" src="http://static.yaoex.com/jsp/common/footer.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx }/static/js/pager.js"></script>
<script type="text/javascript" src="${ctx }/static/js/jquery.form.3.51.0.js"></script>
<script type="text/javascript" src="${ctx }/static/js/order/order_delivery_detail.js"></script>
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