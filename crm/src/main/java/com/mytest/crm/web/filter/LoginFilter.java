package com.mytest.crm.web.filter;

import com.mytest.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //判断有没有session
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        //这个样子会把登录页面也过滤掉
        String path = request.getServletPath();
        //chain.doFilter(req, resp);
        //如果是登录页面就直接放行
        if ("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)) {
            //在获取的路上都要放行
            chain.doFilter(req, resp);
        } else {
            //不是登录页面就走过滤器在放行

            //不为null证明登录过
            if (user != null) {
                //登录过就放行
                chain.doFilter(request, response);
            } else { //为null证明没有登录过，就转发到登录页面
                //动态获取项目名
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }

    }
}
