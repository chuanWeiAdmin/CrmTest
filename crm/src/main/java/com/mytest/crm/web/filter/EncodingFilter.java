package com.mytest.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //在这里过滤乱码的问题
        //过滤请求
        req.setCharacterEncoding("utf-8");

        //过滤响应
        resp.setContentType("text/html;charset=utf-8");

        //放行
        chain.doFilter(req,resp);


    }
}
