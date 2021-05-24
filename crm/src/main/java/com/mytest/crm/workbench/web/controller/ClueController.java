package com.mytest.crm.workbench.web.controller;


import com.mytest.crm.settings.domain.User;
import com.mytest.crm.settings.service.UserService;
import com.mytest.crm.settings.service.impl.UserServiceImpl;
import com.mytest.crm.utils.DateTimeUtil;
import com.mytest.crm.utils.PrintJson;
import com.mytest.crm.utils.UUIDUtil;
import com.mytest.crm.workbench.domain.Activity;
import com.mytest.crm.workbench.domain.Clue;
import com.mytest.crm.workbench.service.ActivityService;
import com.mytest.crm.workbench.service.ClueService;
import com.mytest.crm.workbench.service.impl.ActivitySweviceImpl;
import com.mytest.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/clue/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/clue/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/clue/getActivityListByClueId.do".equals(path)) {
            getActivityListByClueId(request, response);
        }else if ("/workbench/clue/unbund.do".equals(path)) {
            unbund(request, response);
        }

    }

    //打开添加用户的模态窗口的时候要显示用户列表，要查所有用户
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = new UserServiceImpl();
        List<User> uList = userService.getUserList();

        PrintJson.printJsonObj(response, uList);

    }

    //保存用户保存的基本信息
    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");


        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);


        ClueService clueService = new ClueServiceImpl();

        boolean flag = clueService.save(c);
        PrintJson.printJsonFlag(response, flag);

    }

    //转到详细信息页
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ClueService cs = new ClueServiceImpl();
        Clue clue = cs.detail(id);
        request.setAttribute("clue", clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);

    }

    //根据ID查询出想对应的市场活动
    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        String clueId = request.getParameter("id");
        ActivityService as = new ActivitySweviceImpl();
        List<Activity> aList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response,aList);

    }

    //解除关联的方法
    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        String id =request.getParameter("id");
        ClueService cs=new ClueServiceImpl();
        boolean flag =cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }



}
