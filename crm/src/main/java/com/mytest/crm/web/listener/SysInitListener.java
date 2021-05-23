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
        //连接数据库获取数据字典
        System.out.println("服务器缓存处理数据字典开始");

        //获取 application 对象
        ServletContext application = event.getServletContext();

        //调 service 获取数据  暂时不通过动态代理获取
        DicService dicService = new DicServiceImpl();
        Map<String, List<DicValue>> dMap = dicService.getAll();

        //拆map 将数据保存到application中
        Set<String> keys = dMap.keySet();
        for (String key : keys) {
            application.setAttribute(key,dMap.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");

    }

}
