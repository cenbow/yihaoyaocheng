<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
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
    <link href="/static/css/common.css" rel="stylesheet" />

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
                                class="red margin-r-10">${orderDetailsDto.orderStatusName}</span>
                            <c:if test="${orderDetailsDto.orderStatus==9}">
                            <a class="undeline">查看拒收订单</a></div>
                        </c:if>
                        <c:if test="${orderDetailsDto.orderStatus==10}">
                        <a class="undeline">查看补货订单</a></div>
                    </c:if>
                </div>
            </div>
            <div class="container-fluid progress">
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

                        <div class="col-xs-3 control-label text-left">${orderDetailsDto.payTime}<a
                                class="undeline margin-l-10 gathering">收款详情</a></div>
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
                                <c:if test="${orderDetailsDto.payStatus==1}">增值税专用发票</c:if>
                                <c:if test="${orderDetailsDto.payStatus==2}">增值税普通发票</c:if>
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
                <c:choose>
                    <c:when test="${orderDetailsDto != null && fn:length(orderDetailsDto.details) gt 0 }">
                        <c:forEach var="details" items="${orderDetailsDto.details}" varStatus="detailsVarStatus">
                            <input type="hidden" name="productId" value="${orderDetailsDto.details.orderDetailId}"/>
                            <%--  <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].id" id="${shoppingCartDto.productId}" value="${shoppingCartDto.productId}"/>
                              <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productCount" value="${shoppingCartDto.productCount}"/>
                              <input type="hidden" name="orderDtoList[${shoppingCartVarStatus.index}].productInfoDtoList[${shoppingCartDtoVarStatus.index}].productPrice" value="${shoppingCartDto.productPrice}"/>--%>
                            <
                            <tr>
                                <td>
                                    <div class="clearfix">
                                        <div class="fl"><img src="images/img_03.jpg"/></div>
                                        <div class="fl fontbox">
                                            <p class="title">${details.productName}</p>

                                            <p class="text">${details.manufactures}</p>

                                            <p class="text">${details.specification}</p>
                                        </div>
                                    </div>
                                </td>
                                <td>¥ ${details.productPrice}</td>
                                <td>x${details.productCount}</td>
                                <td>¥ ${details.productPrice * details.productCount}</td>
                                <td>x ${details.recieveCount}</td>
                                <td>¥ ${details.productPrice * details.recieveCount}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                </c:choose>
                <%--遍历该供应商的商品信息  结束--%>
                </tbody>
            </table>
            <div><a class="undeline lookgoodlist">查看收货商品清单</a></div>
            <div class="text-right">
                <p>商品金额：${orderDetailsDto.orderTotal}元

                <p>

                <p>确认收货商品金额：${orderDetailsDto.orderTotal}元

                <p>

                <p>优惠券：- 00.00元

                <p>

                <p class="red">订单金额：${orderDetailsDto.orgTotal}元

                <p>

                <p class="red">结算订单金额：${orderDetailsDto.orgTotal}元

                <p>
            </div>
            <div class="pager" id="J_pager" current="3" total="20" url="http://www.baidu.com"><a
                    href="javascript:void(0)" class="pager_prev">上一页</a><a href="javascript:void(0)" class="pager_item">1</a><a
                    href="javascript:void(0)" class="pager_item">2</a><a href="javascript:void(0)"
                                                                         class="pager_item active">3</a><a
                    href="javascript:void(0)" class="pager_item">4</a><a href="javascript:void(0)"
                                                                         class="pager_item">5</a><a
                    href="javascript:void(0)" class="pager_item">6</a><a href="javascript:void(0)"
                                                                         class="pager_item">7</a><a
                    href="javascript:void(0)" class="pager_item">8</a><span class="pager_dot">...</span><a
                    href="javascript:void(0)" class="pager_item">20</a><a href="javascript:void(0)" class="pager_next">下一页</a><span
                    class="page_total">共<em>20</em>页</span><label class="form_pageJump"><span>到<input type="text"
                                                                                                      name="page"
                                                                                                      class="input_item input_item_shortest page-num"
                                                                                                      autocomplete="off"
                                                                                                      id="page-num0">页</span><a
                    href="javascript:void(0)" class="btn_blue btn_submit" data-form-button="submit">确定</a></label></div>
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

                        <div class="col-xs-5 control-label text-left">500.00 元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">实际收款金额</label>

                        <div class="col-xs-5 control-label text-left">500.00 元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">应收实收差异</label>

                        <div class="col-xs-5 control-label text-left">500.00 元</div>
                    </div>
                    <div class="form-group">
                        <label for="scope" class="col-xs-3 control-label">备注</label>

                        <div class="col-xs-5 control-label text-left">500.00 元</div>
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
                <table class="table table-box">
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
                    <tr>
                        <td>1000000000007</td>
                        <td>p123456789</td>
                        <td>0253265</td>
                        <td>葵花牌葵花牌葵</td>
                        <td>小儿感冒颗粒</td>
                        <td>10gx10包</td>
                        <td>颗粒剂</td>
                        <td>黑龙江哈药六盘...</td>
                        <td>10</td>
                        <td>10</td>
                    </tr>
                    </tbody>
                </table>
                <div class="pager" id="J_pager" current="3" total="20" url="http://www.baidu.com"><a
                        href="javascript:void(0)" class="pager_prev">上一页</a><a href="javascript:void(0)"
                                                                               class="pager_item">1</a><a
                        href="javascript:void(0)" class="pager_item">2</a><a href="javascript:void(0)"
                                                                             class="pager_item active">3</a><a
                        href="javascript:void(0)" class="pager_item">4</a><a href="javascript:void(0)"
                                                                             class="pager_item">5</a><a
                        href="javascript:void(0)" class="pager_item">6</a><a href="javascript:void(0)"
                                                                             class="pager_item">7</a><a
                        href="javascript:void(0)" class="pager_item">8</a><span class="pager_dot">...</span><a
                        href="javascript:void(0)" class="pager_item">20</a><a href="javascript:void(0)"
                                                                              class="pager_next">下一页</a><span
                        class="page_total">共<em>20</em>页</span><label class="form_pageJump"><span>到<input type="text"
                                                                                                          name="page"
                                                                                                          class="input_item input_item_shortest page-num"
                                                                                                          autocomplete="off"
                                                                                                          id="page-num0">页</span><a
                        href="javascript:void(0)" class="btn_blue btn_submit" data-form-button="submit">确定</a></label>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<!--#include file="footer.asp" -->
<script type="text/javascript" src="http://static.yaoex.com/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://static.yaoex.com/js/My97DatePicker/WdatePicker.js"></script>
<script src="http://static.yaoex.com/js/jquery-1.11.3.min.js" type="text/javascript" ></script>
<script src="http://static.yaoex.com/js/arttemplate.min.js" type="text/javascript" ></script>
<script src="/static/js/menu_data.js"></script>
<script>
    $(".gathering").click(function () {
        $("#myModal1").modal();
    });
    $(".lookgoodlist").click(function () {
        $("#myModal2").modal();
    });
    $("#delete").click(function () {
        alertModal("确定要删除吗？");
    });
</script>

</body>


</html>