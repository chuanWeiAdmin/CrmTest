package com.mytest.crm.web.listener;

import com.mytest.crm.settings.domain.DicValue;
import com.mytest.crm.settings.service.DicService;
import com.mytest.crm.settings.service.impl.DicServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        //在项目初始化的时候创建
        System.out.println("服务器缓存处理数据字典开始");
        ServletContext application= event.getServletContext();

        DicService dicService=new DicServiceImpl();
        Map<String, List<DicValue>> map= dicService.getAll();
        Set<String> set = map.keySet();
        for(String key:set){

            application.setAttribute(key, map.get(key));

        }
        System.out.println("服务器缓存处理数据字典结束");
    }
}
