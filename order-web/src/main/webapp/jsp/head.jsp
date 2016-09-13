<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/4
  Time: 17:22
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="top">
    <div class="wapper">
        <div class="top-side">
            <ul>
                <li>
                    <c:choose>
                        <c:when test="${loginUserDto != null }">
                            <a href="#" class="pl25">您好，${loginUserDto != null ? loginUserDto.custName : ""}</a>
                            <a href="#" style="padding-right: 10px">退出</a>
                        </c:when>
                        <c:otherwise>
                            您好，欢迎您来到1号药城药品信息网
                            <a href="#" class="red pl25">请登录</a>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li><a href="${ctx}/order/buyerOrderManage">我的订单</a></li>
                <li><a href=${ctx}/order/buyerOrderManage">我的1号药城</a></li>
                <li><a href="#">帮助</a></li>
                <li class="last-li">
                    <i class="common-icon top-phone"></i>
                    <span class="red">4009215767</span>
                </li>
            </ul>
        </div>
    </div>
</div>
