<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>


    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


    <script type="text/javascript">

        $(function () {
            //下面分页查询的方法  第一个是页数，第二个是一页显示多少
            //页面加载结束
            pageList(1, 2);

            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            //点击查询按钮执行查询方法
            $("#searchBtn").click(function () {

                //点击查询的时候将数据赋值给隐藏域
                $("#hidden-name").val($("#search-name").val())
                $("#hidden-owner").val($("#search-owner").val())
                $("#hidden-startDate").val($("#search-startDate").val())
                $("#hidden-endDate").val($("#search-endDate").val())

                pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
            });

            //点击新建按钮，查询数据库，打开模态框
            $("#addDataBtn").click(function () {

                //这里应该进入后台controller查询全部人员的信息
                $.ajax({
                    url: "workbench/activity/getUserList.do",
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        //console.log(data);
                        var htmlSelect = "<option></option>";
                        $.each(data, function (index, value) {
                            htmlSelect += " <option value='" + value.id + "'>" + value.name + "</option>";

                        })

                        $("#create-owner").html(htmlSelect);

                        //使用EL表达式设置默认选择
                        var id = "${user.id}"
                        //console.log(id);
                        $("#create-owner").val(id);

                        //所有者下拉框处理完毕后，展现模态窗口
                        $("#createActivityModal").modal("show");


                    },
                    error: function (data) {
                        alert("异常");
                    }
                })


            })

            //为新建的保存按钮绑定事件，执行添加操作
            $("#saveBtn").click(function () {

                $.ajax({
                    url: "workbench/activity/save.do",
                    data: {
                        "owner": $.trim($("#create-owner").val()),
                        "name": $.trim($("#create-name").val()),
                        "startDate": $.trim($("#create-startDate").val()),
                        "endDate": $.trim($("#create-endDate").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-description").val())

                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        console.log(data);
                        if (data.success = true) {

                            //清空模态框中form选择的数据
                            $("#activityAddForm")[0].reset();
                            //关闭模态框
                            $("#createActivityModal").modal("hide");

                            //添加结束应该刷新一下
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                , $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                        } else {

                            alert("添加数据失败");
                        }

                    }
                })


            })

            //点击删除按钮
            $("#deleteBtn").click(function () {
                var $xz = $("input[name=xz]:checked");

                if ($xz.length == 0) {
                    alert("请选择要删除的信息");
                } else {
                    //已经选择了要删除的信息

                    if (confirm("确定删除吗？？？？")) {
                        // 拼接参数
                        var param = "";
                        for (var i = 0; i < $xz.length; i++) {
                            param += "id=" + $($xz[i]).val();
                            if (i < $xz.length - 1) {
                                param += "&";
                            }
                        }

                        //var tmp={"id":[1,2,3,4,5,6]};

                        $.ajax({
                            url: "workbench/activity/delete.do",
                            data: param,
                            type: "get",
                            dataType: "json",
                            success: function (data) {
                                //分析一下返回的是什么？？
                                //删除应该就是一个状态，成功，失败
                                //{"success":true/false}
                                if (data.success) {
                                    pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                                } else {
                                    alert("删除失败");
                                }

                            },
                            error: function (data) {
                                alert("异常");
                            }
                        })

                    }//if(confirm("确定删除吗？？？？")){ 点击确认按钮


                }//if ($xz.length==0){  else

            })//$("#deleteBtn").click(function () {

            //点击修改按钮
            $("#editBtn").click(function () {
                var $xz = $("input[name=xz]:checked");
                //这一定是选择一个
                if ($xz.length == 0) {
                    alert("请选择要修改的信息");
                } else if ($xz.length > 1) {
                    alert("请选择一条数据更改");
                } else {
                    var id = $xz.val();

                    $.ajax({
                        url: "workbench/activity/getUserListAndActivity.do",
                        data: {"id": id},
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            /*
                            * 分析一下data 应该返回什么类型的数据
                            * 首先应该有所有者列表和市场活动基本信息
                            * {uList:[{所有者1},{所有者2},{所有者3}],"activity":{基本信息}}
                            * */

                            //添加数据
                            var htmlSelect = "<option></option>";
                            $.each(data.uList, function (index, value) {
                                htmlSelect += " <option value='" + value.id + "'>" + value.name + "</option>";
                            })
                            //添加select
                            $("#edit-owner").html(htmlSelect);

                            //下列input框赋值
                            $("#edit-id").val(data.activity.id);
                            $("#edit-owner").val(data.activity.owner);
                            $("#edit-name").val(data.activity.name);
                            $("#edit-startDate").val(data.activity.startDate);
                            $("#edit-endDate").val(data.activity.endDate);
                            $("#edit-cost").val(data.activity.cost);
                            $("#edit-description").val(data.activity.description);

                            //打开模态窗口
                            $("#editActivityModal").modal("show");

                        },
                        error: function (data) {
                            console.log(data)
                            alert("异常");
                        }
                    })//$.ajax({

                }// if ($xz.length = 0) {   else

            })// $("#editBtn").click(function () {  点击修改按钮

            //点击修改的保存按钮，保存修改，修改一定要提交
            $("#updateBtn").click(function () {

                $.ajax({
                    url: "workbench/activity/update.do",
                    data: {
                        "id":$.trim($("#edit-id").val()),
                        "owner": $.trim($("#edit-owner").val()),
                        "name": $.trim($("#edit-name").val()),
                        "startDate": $.trim($("#edit-startDate").val()),
                        "endDate": $.trim($("#edit-endDate").val()),
                        "cost": $.trim($("#edit-cost").val()),
                        "description": $.trim($("#edit-description").val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {

                        if (data.success = true) {

                            //关闭模态框
                            $("#editActivityModal").modal("hide");

                            //添加结束应该刷新一下
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                , $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                        } else {

                            alert("添加数据失败");
                        }

                    }
                })
            })


            //全选，全不选方法
            $("#qx").click(function () {
                $("input[name=xz]").prop("checked", this.checked);
                //$("input[name=xz]").prop("checked",this.checked);
            })
            $("#activityBody").on("click", $("input[name=xz]"), function () {
                $("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length);
            })

        });// $(function () {  页面加载完毕执行的方法

        //页面分页查询的方法，
        /*
        1.页面加载结束
        2.点击查询按钮
        3.点击创建，修改，删除
        4.点击下一页
        * */
        function pageList(pageNo, pageSize) {
            //每一次执行分页查询的时候再将数据赋值回来
            $("#search-name").val($("#hidden-name").val())
            $("#search-owner").val($("#hidden-owner").val())
            $("#search-startDate").val($("#hidden-startDate").val())
            $("#search-endDate").val($("#hidden-endDate").val())

            $.ajax({
                url: "workbench/activity/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "name": $.trim($("#search-name").val()),
                    "owner": $.trim($("#search-owner").val()),
                    "startDate": $.trim($("#search-startDate").val()),
                    "endDate": $.trim($("#search-endDate").val())
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    /*
                    * 返回的数据类型
                    * 市场活动数据
                    * [{市场活动1}，{市场活动2}，{市场活动3}，{市场活动4}]
                    * 总条数数据
                    * {total:total}
                    *
                    * 将两种数据类型合并
                    * {
                    *   total:total,
                    *   dataList:[{市场活动1}，{市场活动2}，{市场活动3}，{市场活动4}]
                    * }
                    *
                    * */
                    var tableHtml = "";
                    $.each(data.dataList, function (index, value) {
                        tableHtml += ' <tr class="active" >'
                        tableHtml += ' <td><input type="checkbox" name="xz" value="' + value.id + '"/></td>'
                        tableHtml += ' <td><a style="text-decoration: none; cursor: pointer;"onclick="window.location.href=\'workbench/activity/detail.do?id='+value.id+'\';">' + value.name + '</a></td>'
                        tableHtml += ' <td>' + value.owner + '</td>'
                        tableHtml += ' <td>' + value.startDate + '</td>'
                        tableHtml += ' <td>' + value.endDate + '</td>'
                        tableHtml += ' </tr>'
                    })
                    //获得到tbody对象，将table字符串添加到对象的HTML中
                    $("#activityBody").html(tableHtml);

                    //计算总页数
                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;
                    //在pageList.do处理ajax返回值后，加入分页组件
                    //数据处理完毕后，结合分页查询，对前端展现分页信息
                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,

                        //该回调函数时在，点击分页组件的时候触发的
                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    });


                },
                error: function (data) {
                    console.log(data);
                    alert("异常");
                }

            })//$.ajax({

        }//function pageList(pageNo, pageSize) {


    </script>

</head>
<body>
<%--隐藏域--%>
<input type="hidden" id="hidden-name"/>
<input type="hidden" id="hidden-owner"/>
<input type="hidden" id="hidden-startDate"/>
<input type="hidden" id="hidden-endDate"/>


<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="activityAddForm" role="form">

                    <div class="form-group">
                        <label for="create-Owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">
                            </select>
                        </div>
                        <label for="create-name" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate">
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id"/>
                    <div class="form-group">
                        <label for="edit-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">
                            </select>
                        </div>
                        <label for="edit-name" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startDate">
                        </div>
                        <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endDate">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control time" type="text" id="search-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control time" type="text" id="search-endDate">
                    </div>
                </div>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addDataBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="qx"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">

                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">

            <div id="activityPage"></div>

        </div>

    </div>

</div>

</body>
</html>