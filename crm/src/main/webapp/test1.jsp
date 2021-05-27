<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
</head>
<body>
<script>
    function test() {
        $.ajax({
            url: "",
            data: {},
            type: "",
            dataType: "json",
            success: function (data) {

            },
            error : function (data){
                console.log(data)
                alert("异常");
            }
        })



    }
    String createBy = ((User)request.getSession().getAttribute("user")).getName();
</script>
</body>
</html>
