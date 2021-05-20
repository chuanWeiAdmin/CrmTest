package com.mytest.crm.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionUtil {

    private SqlSessionUtil() {
    }


    private static SqlSessionFactory factory;

    static {

        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        factory = new SqlSessionFactoryBuilder().build(inputStream);

    }

    //这里有问题  一直没搞懂
	/*
	问题原因：
		通过代理模式的情况下获取sqlSession获取的是不同的导致程序报错。
		还没有找到问题解决方法
	* */
    private static ThreadLocal<SqlSession> t = new ThreadLocal<>();

    public static SqlSession getSqlSession() {
        //高级版本有问题（通过动态代理获取ThreadLocal每一次获取的都是不同的）
//        SqlSession session = t.get();
//        if (session == null) {
//            session = factory.openSession();
//            t.set(session);
//        }
//        return session;

        //low的版本不使用动态代理
        return factory.openSession();
    }

    public static void myClose(SqlSession session) {

        if (session != null) {
            session.close();
            t.remove();
        }

    }


}
