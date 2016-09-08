<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="static com.yyw.yhyc.order.enmu.BillTypeEnum.BillTypeSpecial" %>
<%@ page import="static com.yyw.yhyc.order.enmu.BillTypeEnum.BillTypeNormal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>退货订单详情</title>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/header.js"></script>
    <script type="text/javascript" src="http://static.yaoex.com/jsp/common/sidebar.js"></script>
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
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>采购订单管理</a></li>
                    <li><a href="#"><i class="fa fa-map-marker fa-3"></i>退货订单管理</a></li>
                    <li class="active">订单详情</li>
                </ol>
            </div>
            <div class="border-gray">
                <div class="form-horizontal padding-t-26">
                    <div class="form-group">
                        <label class="col-xs-2 control-label">退货订单号：</label>
                        <div class="col-xs-2 control-label text-left">${orderExceptionDto.exceptionOrderId}</div>
                        <label class="col-xs-2 control-label">订单状态：</label>
                        <div class="col-xs-2 control-label text-left"><span class="red margin-r-10">${orderExceptionDto.orderStatusName}</span> </div>
                        <label class="col-xs-2 control-label">原订单号：</label>
                        <div class="col-xs-2 control-label text-left"><a class="undeline" target="_blank" href="${ctx}/order/getBuyOrderDetails?flowId=${orderExceptionDto.flowId}">${orderExceptionDto.flowId}</a></div>
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
                            <label for="scope" class="col-xs-2 control-label">收货人：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.receivePerson}</div>
                            <label for="scope" class="col-xs-2 control-label">收货地址：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.receiveAddress}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">联系方式：</label>
                            <div class="col-xs-10 control-label text-left">${orderExceptionDto.orderDelivery.receiveContactPhone}</div>
                        </div>
                    </div>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label class="col-xs-12 color999 padding-l-40 font-size-16">发货人信息</label>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">供应商：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.usermanageEnterprise.enterpriseName}</div>
                            <label for="scope" class="col-xs-2 control-label">发货人：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.deliveryPerson}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">联系方式：</label>
                            <div class="col-xs-10 control-label text-left">${orderExceptionDto.orderDelivery.deliveryContactPhone}</div>
                        </div>
                    </div>
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label class="col-xs-12 color999 padding-l-40 font-size-16">其他信息</label>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">关联订单支付方式：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.payTypeName}</div>
                            <label for="scope" class="col-xs-2 control-label">发票信息：</label>
                            <div class="col-xs-3 control-label text-left">
                                <c:set var="billTypeSpecial" value="<%=BillTypeSpecial.getBillType()%>"></c:set>
                                <c:set var="billTypeNormal" value="<%=BillTypeNormal.getBillType()%>"></c:set>
                                <c:choose>
                                    <c:when test="${orderExceptionDto.order.billType == billTypeSpecial}">
                                        <%=BillTypeSpecial.getBillTypeName()%>
                                    </c:when>
                                    <c:otherwise>
                                        <%=BillTypeNormal.getBillTypeName()%>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">退货时间：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.createTime}</div>
                            <label for="scope" class="col-xs-2 control-label">退货说明：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.returnDesc}</div>
                        </div>

                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">卖家确认/关闭时间：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.updateTime}</div>
                            <label for="scope" class="col-xs-2 control-label">确认/关闭说明：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.remark}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">买家发货时间：</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.createTime}</div>
                            <label for="scope" class="col-xs-2 control-label">卖家确认收货时间</label>
                            <div class="col-xs-3 control-label text-left">${orderExceptionDto.receiveTime}</div>
                        </div>
                        <div class="form-group">
                            <label for="scope" class="col-xs-2 control-label">退款时间：</label>
                            <div class="col-xs-3 control-label text-left">
                                ${(orderExceptionDto.orderSettlement.refunSettlementMoney != null&&orderExceptionDto.orderSettlement.refunSettlementMoney!="")?orderExceptionDto.orderSettlement.settlementTime:"" }
                            </div>
                            <label for="scope" class="col-xs-2 control-label">
                                取消退货时间：

                               </label>
                            <div class="col-xs-3 control-label text-left">
                                ${(orderExceptionDto.orderStatus == "2" || orderExceptionDto.orderStatus=="4")?orderExceptionDto.updateTime:""}
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <div class="row choseuser margin-t-20 border-gray">
                <h2 class="row">配送信息</h2>
                <div class="modify">
                    <div class="form-horizontal padding-t-26">
                        <div class="form-group">
                            <label class="col-xs-12 color999 padding-l-40 font-size-16">配送信息</label>
                        </div>
                        <c:choose>
                            <c:when test='${orderExceptionDto.orderDelivery !=null && orderExceptionDto.orderDelivery.deliveryMethod eq "1"}'>
                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">配送方式：</label>
                                    <div class="col-xs-3 control-label text-left">自有物流</div>
                                    <label for="scope" class="col-xs-2 control-label">预计送达时间：</label>
                                    <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.deliveryDate}</div>
                                </div>
                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">联系人：</label>
                                    <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.deliveryPerson}</div>
                                    <label for="scope" class="col-xs-2 control-label">联系电话：</label>
                                    <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.deliveryContactPhone}</div>
                                </div>
                            </c:when>
                            <c:when test='${orderExceptionDto.orderDelivery !=null && orderExceptionDto.orderDelivery.deliveryMethod eq "2"}'>
                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">配送方式：</label>
                                    <div class="col-xs-3 control-label text-left">第三方物流</div>
                                    <label for="scope" class="col-xs-2 control-label">物流公司：</label>
                                    <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.deliveryContactPerson}</div>
                                </div>
                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">物流单号：</label>
                                    <div class="col-xs-3 control-label text-left">${orderExceptionDto.orderDelivery.deliveryExpressNo}</div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="form-group">
                                    <label for="scope" class="col-xs-2 control-label">配送方式：</label>
                                    <div class="col-xs-3 control-label text-left">暂无配送信息</div>
                                    <label for="scope" class="col-xs-2 control-label">退货说明：</label>
                                    <div class="col-xs-3 control-label text-left">暂无配送信息</div>
                                </div>
                            </c:otherwise>
                        </c:choose>



                    </div>
                </div>
            </div>
            <div class="row choseuser margin-t-20 border-gray">
                <h2 class="row">退货商品清单</h2>
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
                                                    <img alt="${orderReturnDto.productName}" class="productImageUrl" spuCode="${orderReturnDto.spuCode}"  onerror="this.error = null;this.src='${STATIC_URL}/static/images/img_03.jpg'">
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
                                        暂无退货商品信息
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
<div class="footer">
    <p>监管机构：国家食品药品监督管理局   广东省食品药品监督管理局   广州市食品药品监督管理局</p>
    <p>Copyright (C) 2010-2015 本网站版权归广东壹号药业有限公司所有  网站备案号:粤ICP备15048678</p>
</div>
<!--框架右侧内容 end-->
</div>
</body>
<script type="text/javascript" src="${STATIC_URL}/static/js/jquery-1.12.1.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/common.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/b_common.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/My97DatePicker/WdatePicker.js"></script>
</html>


