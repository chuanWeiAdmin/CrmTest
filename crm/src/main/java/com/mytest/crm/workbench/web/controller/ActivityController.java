package com.mytest.crm.workbench.web.controller;

import com.mytest.crm.settings.domain.User;
import com.mytest.crm.settings.service.UserService;
import com.mytest.crm.settings.service.impl.UserServiceImpl;
import com.mytest.crm.utils.DateTimeUtil;
import com.mytest.crm.utils.PrintJson;
import com.mytest.crm.utils.UUIDUtil;
import com.mytest.crm.vo.PaginationVO;
import com.mytest.crm.workbench.domain.Activity;
import com.mytest.crm.workbench.domain.ActivityRemark;
import com.mytest.crm.workbench.service.ActivityService;
import com.mytest.crm.workbench.service.impl.ActivitySweviceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/activity/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            pageList(request, response);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            delete(request, response);
        } else if ("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            getUserListAndActivity(request, response);
        } else if ("/workbench/activity/update.do".equals(path)) {
            update(request, response);
        } else if ("/workbench/activity/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/activity/getRemarkListByAid.do".equals(path)) {
            getRemarkListByAid(request, response);
        } else if ("/workbench/activity/deleteRemark.do".equals(path)) {
            deleteRemark(request, response);
        } else if ("/workbench/activity/saveRemark.do".equals(path)) {
            saveRemark(request, response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)) {
            updateRemark(request, response);
        }

    }
    //查询全部用户
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        //通过工具获得对象----->还有问题
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        UserService us = new UserServiceImpl();

        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response, uList);

    }

    //添加市场活动
    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User) request.getSession().getAttribute("user")).getName();


        //将获取的参数保存为对象
        Activity a = new Activity();

        a.setId(id);
        a.setCost(cost);
        a.setStartDate(startDate);
        a.setOwner(owner);
        a.setName(name);
        a.setEndDate(endDate);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        //暂时不通过动态代理执行提交动作，因为还有个地方没有弄明白
        //ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ActivityService as = new ActivitySweviceImpl();
        //添加动作一定要执行      提交    动作
        boolean flag = as.save(a);
        PrintJson.printJsonFlag(response, flag);
    }

    //分页查询的方法
    private void pageList(HttpServletRequest request, HttpServletResponse response) {


        String name = request.getParameter("name");
        String owner = request.getParameter("owner");     //这一项一定要注意，是前写的所有者的名字，并不是owner id 号
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);

        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        /*
         * 分析一下pageNo和pageSize
         * pageNo是页数
         * pageSize是每页显示的个数
         *
         * 需要的是两个参数 略过的条数，每页显示的个数
         * */
        int skipCount = (pageNo - 1) * pageSize;

        //mybatis没有办法接受多个类型
        //可以打包成一个map
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        //map集合有了应该返回什么？？？？？
        //一个list + int
        //因为多处使用了，应该使用vo来将数据打到前端


        //调用service执行业务
        //这样通过动态代理还有问题
        //ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivitySweviceImpl());
        ActivityService activityService = new ActivitySweviceImpl();  //直接new 简单粗暴，别整没用的

        PaginationVO<Activity> pv = activityService.pageList(map);
        PrintJson.printJsonObj(response, pv);

    }

    //删除按钮的方法
    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的删除操作");

        //取参数
        String[] ids = request.getParameterValues("id");

        //调用service执行业务  因为动态代理有问题，就通过正常的方法调用
        //ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ActivityService as = new ActivitySweviceImpl();
        boolean falg = as.delete(ids);


        PrintJson.printJsonFlag(response, falg);

    }

    //修改按钮的方法
    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");

        String id = request.getParameter("id");

        //我要查两个东西，一个所有者，一个根据id查基本信息
        System.out.println("执行市场活动添加操作");

        ActivityService activityService = new ActivitySweviceImpl();

        //应该返回一个map
        Map<String, Object> map = activityService.getUserListAndActivity(id);

        PrintJson.printJsonObj(response, map);


    }

    //修改按钮中的更新数据
    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        //修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        //将获取的参数保存为对象
        Activity a = new Activity();

        a.setId(id);
        a.setCost(cost);
        a.setStartDate(startDate);
        a.setOwner(owner);
        a.setName(name);
        a.setEndDate(endDate);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        //暂时不通过动态代理执行提交动作，因为还有个地方没有弄明白
        //ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ActivityService as = new ActivitySweviceImpl();
        //添加动作一定要执行      提交    动作
        boolean flag = as.update(a);
        PrintJson.printJsonFlag(response, flag);


    }

    //点击项目的名称进入备注列表（传统请求）
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //是传统请求要用到转发，将数据放到request域中
        String id = request.getParameter("id");

        //不使用动态代理
        ActivityService activityService = new ActivitySweviceImpl();
        Activity activity = activityService.detail(id);

        request.setAttribute("activity", activity);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);


    }

    //根据ID查备注
    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService activityService = new ActivitySweviceImpl();
        List<ActivityRemark> arList = activityService.getRemarkListByAid(id);

        PrintJson.printJsonObj(response, arList);

    }

    //根据ID删除备注信息
    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        ActivityService activityService = new ActivitySweviceImpl();
        boolean flag = activityService.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);

    }

    //保存备注
    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("remark");
        String createTime = DateTimeUtil.getSysTime();
        String createBy=((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar= new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);

        ActivityService activityService = new ActivitySweviceImpl();

        boolean flag= activityService.saveRemark(ar);

        Map<String ,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);

    }

    //修改备注
    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        String id=request.getParameter("id");
        String noteContent=request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";


        ActivityRemark ar = new ActivityRemark();

        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);

        ActivityService activityService= new ActivitySweviceImpl();
        boolean flag= activityService.updateRemark(ar);

        Map<String,Object>map =new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);

    }



}
