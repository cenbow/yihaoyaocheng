<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="static com.yyw.yhyc.order.enmu.BillTypeEnum.BillTypeSpecial" %>
<%@ page import="static com.yyw.yhyc.order.enmu.BillTypeEnum.BillTypeNormal" %>
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
                        <li><a href="#"><i class="fa fa-map-marker fa-3"></i>首页</a></li>
                        <li><a href="${ctx}/orderException/sellerRefundOrderManage"><i class="fa fa-map-marker fa-3"></i>换货订单管理</a></li>
                        <li class="active">审核订单</li>
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
                            <div class="col-xs-2 control-label text-left"><a class="undeline" target="_blank" href="${ctx}/order/getSupplyOrderDetails?flowId=${orderExceptionDto.flowId}">${orderExceptionDto.flowId}</a></div>
                        </div>
                    </div>
                </div>
                <div class="row choseuser margin-t-20 border-gray">
                    <div class="form-horizontal padding-t-26">
                            <input type="hidden" value="${orderExceptionDto.exceptionId}" id="exceptionId"/>
                            <div class="form-group">
                                <label class="col-xs-2 control-label">买家换货说明:</label>
                                <div class="col-xs-9 control-label text-left">${orderExceptionDto.returnDesc}</div>
                            </div>
                            <div class="form-group">
                                <label  class="col-xs-2 control-label">审核说明：</label>
                                <div class="col-xs-9">
                                    <textarea class="form-control" rows="5" id="remark" maxlength="50"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
	                               <label for="scope" class="col-xs-2 control-label">换货收货地址:</label>
		                            <div class="col-xs-9 control-label text-left" id="warehouse" >
		                            
		                            
		                      <c:if test="${orderExceptionDto.receiverAddressList != null && fn:length(orderExceptionDto.receiverAddressList) gt 0 }">
                                      <c:forEach items="${orderExceptionDto.receiverAddressList}" var="item" varStatus="status">
				                       <div>
			                               <label>
												<c:if test="${item.defaultAddress==1}">
												<input type="radio" checked="true" name="delivery" value="${item.id}"/>
												 ${item.provinceName}${item.cityName}${item.districtName}${item.address}&nbsp;&nbsp;${item.receiverName}&nbsp;&nbsp;${item.contactPhone}
												</c:if>
												<c:if test="${item.defaultAddress==0}">
												<input type="radio" name="delivery" value="${item.id}"/>
												 ${item.provinceName}${item.cityName}${item.districtName}${item.address}&nbsp;&nbsp;${item.receiverName}&nbsp;&nbsp;${item.contactPhone}
												</c:if>
									       </label>
								       </div>
                                     </c:forEach>
                                </c:if>
		                             
		                            </div>
                             </div>
                            <div class="form-group">
                                <label class="col-xs-2"></label>
                                <div class="col-xs-1"><button type="button" class="btn btn-danger" onclick="review(4)">通过</button></div>
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
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.usermanageEnterprise.enterpriseName}</div>
                                <label for="scope" class="col-xs-2 control-label">发货人</label>
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.usermanageEnterprise.registeredAddress}</div>
                            </div>
                            <div class="form-group">
                                <label for="scope" class="col-xs-2 control-label">联系方式</label>
                                <div class="col-xs-10 control-label text-left">${orderExceptionDto.usermanageEnterprise.enterpriseCellphone}</div>
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
                                <label for="scope" class="col-xs-2 control-label">下单时间</label>
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.createTime}</div>
                                <label for="scope" class="col-xs-2 control-label">换货说明</label>
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.returnDesc}</div>
                            </div>

                            <div class="form-group">
                                <label for="scope" class="col-xs-2 control-label">卖家确认/关闭时间</label>
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.updateTime}</div>
                                <label for="scope" class="col-xs-2 control-label">确认/关闭说明</label>
                                <div class="col-xs-3 control-label text-left">${orderExceptionDto.remark}</div>
                            </div>

                        </div>
                    </div>
                </div>

                <div class="row choseuser margin-t-20 border-gray">
                    <h2 class="row">换货商品清单</h2>
                    <div class="modify padding-20">
                        <table class="table table-box">
                            <colgroup>
                                <col style="width: 3%;"/>
                                <col style="width: 27%;"/>
                                <col style="width: 15%;" />
                                <col style="width: 15%;" />
                                <col style="width: 15%;" />
                                <col style="width: 15%;" />
                            </colgroup>
                            <thead>
                            <tr>
                                <th></th>
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
                                    <c:set var="spuCount" value="0"></c:set>
                                    <c:set var="spuStr" value=","></c:set>
                                    <c:forEach var="orderReturnDto" items="${orderExceptionDto.orderReturnList}" varStatus="detailsVarStatus">
                                        <c:set var="spuCodeThis" value="${orderReturnDto.spuCode}"></c:set>
                                        <c:choose>
                                            <c:when test="${fn:contains(spuStr,spuCodeThis)}">
                                            </c:when> <c:otherwise>
                                            <c:set var="spuCount" value="${spuCount+1}"></c:set>
                                        </c:otherwise>
                                        </c:choose>
                                        <c:set var="spuStr" value="${spuCount}+','+${orderReturnDto.spuCode}"></c:set>
                                        <tr>
                                            <td>${ detailsVarStatus.index + 1}</td>
                                            <td>
                                                <div class="clearfix">
                                                    <div class="fl">
                                                        <a href='http://mall.yaoex.com/product/productDetail/${orderReturnDto.spuCode}/${orderReturnDto.supplyId}'>
                                                        <img src="${orderReturnDto.imageUrl}" alt="${orderReturnDto.productName}"  onerror="this.error = null;this.src='${STATIC_URL}/static/images/img_03.jpg'">
                                                        </a>
                                                    </div>
                                                    <div class="fl fontbox">
                                                        <a href='http://mall.yaoex.com/product/productDetail/${orderReturnDto.spuCode}/${orderReturnDto.supplyId}'>
                                                        <p class="title">${orderReturnDto.productName}</p>
                                                        </a>
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
                                            暂无换货商品信息
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>

                        <div class="text-right">
                            <p>品种数：${spuCount}</p>
                            <p>商品金额： <fmt:formatNumber value="${orderExceptionDto.productPriceCount}" minFractionDigits="2"/>元<p>
                            <p>满减金额： -<fmt:formatNumber value="${orderExceptionDto.orderShareMoney}" minFractionDigits="2"/>元<p>
                            <p class="red">订单金额：<fmt:formatNumber value="${orderExceptionDto.orderPriceCount}" minFractionDigits="2"/>元<p>
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
<script type="text/javascript">
    function review(type){
        if(type == 4 || type == 3){
            var exceptionId = $("#exceptionId").val();
            if(exceptionId == ''){
                alertModal("订单编号错误");
                return;
            }
            var remark = $("#remark").val().trim();
            var data = {exceptionId:exceptionId,remark:remark,orderStatus:type};
            //审核通过,收货地址必须选择
            if(type==4){
           	 var delivery = $("input[type=radio][name=delivery]:checked").val();
           	 if(delivery){
           		data.delivery=delivery;
           	 }else{
           	  alertModal("请选择收货地址!");
              return;
           	 }
           }
            tipLoad();
            $.ajax({
                url: ctx+"/orderException/sellerReviewChangeOrder",
                data: JSON.stringify(data),
                type: 'POST',
                contentType: "application/json;charset=UTF-8",
                success: function (data) {
                    tipRemove();
                    if (data.statusCode || data.message) {
                        alertModal(data.message);
                        return;
                    }
                    alertModal("操作成功");
                    setTimeout(function(){
                        window.location.href=ctx+"/orderException/sellerChangeGoodsOrderManage";
                    },1000)
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    tipRemove();
                    alertModal("审核失败");
                }
            });
        }
    }
</script>
</html>


