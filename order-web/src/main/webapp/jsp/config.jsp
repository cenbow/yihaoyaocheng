<%@ page import="com.yyw.yhyc.utils.MyConfigUtil" %>
<%
	request.setAttribute("ctx", request.getContextPath());
	request.setAttribute("STATIC_URL", MyConfigUtil.STATIC_URL);
	request.setAttribute("PAY_DOMAIN", MyConfigUtil.PAY_DOMAIN);
	String enterpriseId = request.getParameter("enterpriseId");

	
	String domainPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	request.setAttribute("domainPath", domainPath);
%>
<script type="text/javascript">

	var ctx = '${ctx}';
	var enterpriseId = "<%=enterpriseId%>";
	var contextPath = ctx + "/static";
	var domainPath = "<%=domainPath%>";

</script>