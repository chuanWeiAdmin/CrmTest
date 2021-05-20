package com.mytest.crm.settings.web.controller;

import com.mytest.crm.settings.domain.User;
import com.mytest.crm.settings.service.UserService;
import com.mytest.crm.settings.service.impl.UserServiceImpl;
import com.mytest.crm.utils.MD5Util;
import com.mytest.crm.utils.PrintJson;
import com.mytest.crm.utils.ServiceFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进用户servlet");

        //获得请求的路径
        String path = request.getServletPath();

        //特别注意：这里一定有一个“/”
        if ("/settings/user/login.do".equals(path)) {
            //进入一个方法
            //System.out.println("用户进入登录的方法");
            login(request, response);
        } else if ("/settings/user/xxx.do".equals(path)) {
            //进入另一个方法
            System.out.println("进用户第二个方法");
        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        //获取清请求参数
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //要经过md5加密
        loginPwd= MD5Util.getMD5(loginPwd);
        //获取强求的ip
        String ip = request.getRemoteAddr();

        //获得代理形态的service
        //UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        UserService userService = new UserServiceImpl();
        //一定的抛异常对象的
        try {
            User user = userService.login(loginAct, loginPwd, ip);
            request.getSession().setAttribute("user", user);

            //程序运行到这证明没有问题
            //成功的话应该返回的消息 //{"success":true}
            //response.getWriter().println("{\"success\":true}");
            //在工具类中调用方法

            PrintJson.printJsonFlag(response, true);
            //response.getWriter().print("{\"success\":true}");

        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", e.getMessage());

            //response.setContentType("application/json;charset=utf-8");
            PrintJson.printJsonObj(response, map);
        }


    }
}
