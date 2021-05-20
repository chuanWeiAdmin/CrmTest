package com.mytest.crm.settings.service.impl;

import com.mytest.crm.exception.LoginException;
import com.mytest.crm.settings.dao.UserDao;
import com.mytest.crm.settings.domain.User;
import com.mytest.crm.settings.service.UserService;
import com.mytest.crm.utils.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    //private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    //SqlSession session = SqlSessionUtil.getSqlSession();
    //UserDao userDao = session.getMapper(UserDao.class);


    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {



        SqlSession session = SqlSessionUtil.getSqlSession();

        UserDao userDao = session.getMapper(UserDao.class);

        //private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

        //将数据封装成一个map然后调dao
        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);
        //查完数据库了进行一些列的验证
        if (user == null) {
            throw new LoginException("用户名不存在");
        }

        //判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)) {
            throw new LoginException("账号已锁定");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)) {
            //throw new LoginException("ip地址受限");
        }


        //关闭session,这样弄如果抛异常就关闭不上了
        session.close();
        return user;
//        return null;
    }

    @Override
    public List<User> getUserList() {
        SqlSession session = SqlSessionUtil.getSqlSession();
        UserDao userDao = session.getMapper(UserDao.class);
        List<User> userList = userDao.getUserList();

        //关闭session
        session.close();
        return userList;
    }


}
