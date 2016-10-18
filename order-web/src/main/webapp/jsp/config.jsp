<%@ page import="com.yyw.yhyc.utils.MyConfigUtil" %>
<%
    request.setAttribute("ctx", request.getContextPath());
    request.setAttribute("STATIC_URL", MyConfigUtil.STATIC_URL);
    request.setAttribute("PAY_DOMAIN", MyConfigUtil.PAY_DOMAIN);
    String enterpriseId = request.getParameter("enterpriseId");


    String domainPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    request.setAttribute("domainPath", domainPath);

    /* 图片域名 */
    request.setAttribute("imgDomain", MyConfigUtil.IMG_DOMAIN);

    /* 店铺域名 */
    request.setAttribute("mallDomain", MyConfigUtil.MALL_DOMAIN);

%>
<script type="text/javascript">

    var ctx = '${ctx}';
    var enterpriseId = "<%=enterpriseId%>";
    var contextPath = ctx + "/static";
    var domainPath = "<%=domainPath%>";
    var PAY_DOMAIN = "${PAY_DOMAIN}";
    var imgDomain = "${imgDomain}";
    var mallDomain = "${mallDomain}"

</script>