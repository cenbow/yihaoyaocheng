
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
    <title>审核订单</title>
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
                        <li><a href="#"><i class="fa fa-map-marker fa-3"></i>
                            <c:choose>
                                <c:when test="${orderExceptionDto.userType == 1}">
                                    采购订单管理
                                </c:when>
                                <c:when test="${orderExceptionDto.userType == 2}">
                                    供应订单管理
                                </c:when>
                            </c:choose>
                        </a></li>
                        <li class="active">拒收订单管理>订单详情</li>

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
                            <div class="col-xs-2 control-label text-left">${orderExceptionDto.flowId}</div>
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
                                                                <img src="${orderReturnDto.imageUrl}" alt="${orderReturnDto.productName}"  onerror="this.error = null;this.src='${STATIC_URL}/static/images/img_03.jpg'">
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
    <div class="footer">
        <p>监管机构：国家食品药品监督管理局   广东省食品药品监督管理局   广州市食品药品监督管理局</p>
        <p>Copyright (C) 2010-2015 本网站版权归广东壹号药业有限公司所有  网站备案号:粤ICP备15048678</p>
    </div>
    <!--框架右侧内容 end-->
</div>
</body>
<script type="text/javascript" src="${STATIC_URL}/static/js/jquery-1.12.1.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/b_common.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/My97DatePicker/WdatePicker.js"></script>
</html>


