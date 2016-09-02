<%--
  Created by IntelliJ IDEA.
  User: zhangqiang
  Date: 2016/8/5
  Time: 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
<form action="<%=request.getContextPath().toString()%>/doLogin" method="post">
    *用户id:<input type="text" name="custId"/><b>例:10000</b><br/>
    *用户名称:<input type="text" name="custName"/><b>例:上海九州通</b><br/>
    *用户登录名:<input type="text" name="userName"/><b>例:shanghaijiuzhoutong</b><br/>
    <input type="submit" value="登录" />
</form>
</body>
</html>
