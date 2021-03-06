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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        } else if ("/workbench/clue/unbund.do".equals(path)) {
            unbund(request, response);
        } else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)) {
            getActivityListByNameAndNotByClueId(request, response);
        } else if ("/workbench/clue/bund.do".equals(path)) {
            bund(request, response);
        } else if ("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(request, response);
        } else if ("/workbench/clue/convert.do".equals(path)) {
            convert(request, response);
        }

    }
    //??????????????????
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        //????????????????????????
        System.out.println("????????????????????????");
        UserService userService = new UserServiceImpl();
        List<User> uList = userService.getUserList();
        PrintJson.printJsonObj(response, uList);
    }

    //?????????????????????
    private void save(HttpServletRequest request, HttpServletResponse response) {
        //????????????
        System.out.println("????????????????????????");

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
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
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

        ClueService cs = new ClueServiceImpl();
        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);


    }

    //?????????????????????????????????
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("??????????????????????????????");
        String id = request.getParameter("id");

        ClueService clueService = new ClueServiceImpl();
        Clue clue = clueService.detail(id);

        request.setAttribute("clue", clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);

    }


    //g??????ClueId????????????
    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("????????????id?????????????????????????????????");
        String clueId = request.getParameter("clueId");
        ActivityService as = new ActivitySweviceImpl();
        List<Activity> aList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, aList);

    }

    //????????????
    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("????????????????????????");
        String id = request.getParameter("id");
        ClueService cs = new ClueServiceImpl();
        boolean flag = cs.unbund(id);
        PrintJson.printJsonFlag(response, flag);

    }

    //????????????????????????????????????????????????  ?????????????????????????????????????????????????????????????????????????????????
    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");
        ActivityService as = new ActivitySweviceImpl();
        Map<String, String> map = new HashMap<>();
        map.put("aname", aname);
        map.put("clueId", clueId);
        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(response, aList);
    }

    //g??????????????????
    private void bund(HttpServletRequest request, HttpServletResponse response) {
        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");
        ClueService cs = new ClueServiceImpl();
        boolean flag = cs.bund(cid, aids);
        PrintJson.printJsonFlag(response, flag);
    }

    //???????????????????????????????????????????????????????????????????????????
    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String aname = request.getParameter("aname");

        ActivityService as = new ActivitySweviceImpl();
        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response,aList);

    }

    //?????????????????????
    private void convert(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("clueId");
        String money =request.getParameter("money");
        String name =request.getParameter("name");
        String expectedDate =request.getParameter("expectedDate");
        String stage =request.getParameter("stage");
        String activityId =request.getParameter("activityId");


    }


}
