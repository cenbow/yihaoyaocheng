<%@ page import="com.yyw.yhyc.utils.MyConfigUtil" %>
<%
	request.setAttribute("ctx", request.getContextPath());
	request.setAttribute("STATIC_URL", MyConfigUtil.STATIC_URL);
	String enterpriseId = request.getParameter("enterpriseId");

	
	String domainPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	request.setAttribute("domainPath", domainPath);
%>
<script type="text/javascript">

var ctx = '${ctx}';
var enterpriseId = "<%=enterpriseId%>";
var contextPath = ctx + "/static";

</script>