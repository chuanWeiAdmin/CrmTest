package com.mytest.crm.workbench.web.controller;


import com.mytest.crm.settings.domain.User;
import com.mytest.crm.settings.service.UserService;
import com.mytest.crm.settings.service.impl.UserServiceImpl;
import com.mytest.crm.utils.PrintJson;

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
        }

    }

    //打开添加用户的模态窗口的时候要显示用户列表，要查所有用户
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService=new UserServiceImpl();
        List<User> uList= userService.getUserList();

        PrintJson.printJsonObj(response,uList);

    }


}
