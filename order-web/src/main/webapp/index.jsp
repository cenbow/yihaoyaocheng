<%@ page import="com.yhyc.auth.User" %>
<%@ page import="com.yyw.yhyc.helper.UtilHelper" %>
<%@ page import="com.yyw.yhyc.utils.CacheUtil" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Hello World!</h2>
<p>session id --> <%=session.getId()%></p>

<p>登录信息 --> <%=CacheUtil.getSingleton().get("passport" + session.getId())%></p>

<form action="<%=request.getContextPath().toString()%>" method="post">
    *custId:<input type="text" name="custId" value="${custId}"/><br/>
    *userName:<input type="text" name="userName" value="${userName}"/><br/>
    *custType:<input id="radio1" type="radio" name="custType" value="101"><label for="radio1">买家</label><input id="radio2" name="custType" type="radio" value="201"><label for="radio2">卖家</label><input id="radio3" name="custType" type="radio" value="301"><label for="radio3">买卖家</label><br/>
    <input type="submit" value="登录" />
</form>

<%
    User u = new User();
    if(!UtilHelper.isEmpty(request.getParameter("custType")))
        u.setRole_id(Integer.parseInt(request.getParameter("custType")));
    if(!UtilHelper.isEmpty(request.getParameter("custId")))
        u.setEnterprise_id(Integer.parseInt(request.getParameter("custId")));
    if(!UtilHelper.isEmpty(request.getParameter("userName")))
        u.setUsername(request.getParameter("userName"));

    CacheUtil.getSingleton().add("passport" + session.getId(), JSONObject.toJSONString(u), 60 * 60 * 4);
%>
</body>
</html>
