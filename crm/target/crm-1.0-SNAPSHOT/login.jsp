<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<base href="<%=basePath%>">
<head>
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form class="form-horizontal" role="form">
            <%--action="workbench/index.jsp--%>
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" id="loginAct" type="text" placeholder="用户名">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" id="loginPwd" type="password" placeholder="密码">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">

                    <span id="msg" style="color: red;"></span>

                </div>
                <%--在form表单中一定要将按钮设置为button--%>
                <button type="button" class="btn btn-primary btn-lg btn-block" id="loginSubmit"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>

<script>

    //页面加载完毕执行的方法
    $(function () {

        //保持窗口始终在最顶层
        if(window.top!=window){
            window.top.location=window.location;
        }
        //loginAct 加载结束让用户名文本框得到焦点
        $("#loginAct").focus();

        //清空两个文本框的内容
        $("#loginAct").val("");
        $("#loginPwd").val("");

        //为登录按钮绑定事件
        $("#loginSubmit").click(function () {
            //当点击按钮获取参数
            login();
        })

        //点击回车也要进行登录验证(一般情况下，在页面中点击回车的写发就是这个)
        $(window).keydown(function (event) {
            //alert(event.keyCode);
            if (13 == event.keyCode) {
                //alert("点击了回车");
                login();
            }// if (13 == event.keyCode) {
        })

    })

    //方法一定要写在这个（页面加载结束的方法）外面
    function login() {

        //alert("验证登录信息");
        //获取参数信息
        //有可能信息中含有空格要去除空格$.trim(文本)

        // var loginAct = $.trim($("loginAct"));
        // var loginPwd =  $.trim($("loginPwd"));
        var loginAct = $.trim($("#loginAct").val());
        var loginPwd = $.trim($("#loginPwd").val())

        //验证是否为空
        if (loginAct == "" || loginPwd == "") {
            // alert(loginAct);
            // alert("为空");
            $("#msg").html("用户名和密码不能为空");
            return false;
        }

        //程序到这里证明用户名和密码不为空，应该传给服务器进行密码验证
        //通过ajax
        $.ajax({
            url: "settings/user/login.do",//特别注意这里前面一定没有“/”
            data: {"loginAct": loginAct, "loginPwd": loginPwd},
            type: "post",
            dataType: "json",
            success: function (data) {
                //alert("近这了");
                console.log(data);
                //{"success":true,"msg":"???"}
                //如果成功了应该进入主页面，失败了应该提示
                if (data.success) {
                    //进入这里证明后台程序成功了
                    //成功页面
                    window.location.href = "workbench/index.jsp";
                } else {
                    //进入这里证明程序不成功，显示失败信息
                    $("#msg").html(data.msg);
                }
            },
            error:function (data){
                alert("进入了失败方法");
                console.log(data);
            }


        })


    }

</script>
</body>
</html>