package com.mytest.crm.workbench.service.impl;

import com.mytest.crm.settings.dao.UserDao;
import com.mytest.crm.settings.domain.User;
import com.mytest.crm.utils.SqlSessionUtil;
import com.mytest.crm.vo.PaginationVO;
import com.mytest.crm.workbench.dao.ActivityDao;
import com.mytest.crm.workbench.dao.ActivityRemarkDao;
import com.mytest.crm.workbench.domain.Activity;
import com.mytest.crm.workbench.domain.ActivityRemark;
import com.mytest.crm.workbench.service.ActivityService;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySweviceImpl implements ActivityService {

    //添加数据的方法
    @Override
    public boolean save(Activity a) {
        boolean flag = true;
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityDao activityDao = session.getMapper(ActivityDao.class);
        int count = activityDao.save(a);
        if (count == 0) {
            flag = false;

            //暂时先这样写
            session.rollback();
        } else {

            //暂时先这样写
            session.commit();
        }
        //关闭session
        session.close();
        return flag;
    }

    //分页查询使用的方法
    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityDao activityDao = session.getMapper(ActivityDao.class);

        //因为要返回两种类型的数据，所有要用两个dao中的方法
        //获得总数
        int total = activityDao.getTotalByCondition(map);

        //获得基本信息
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //创建一个vo对象用来返回数据
        PaginationVO<Activity> pv = new PaginationVO<>();
        pv.setTotal(total);
        pv.setDataList(dataList);

        //要关闭session
        session.close();


        return pv;
    }

    //删除用户的方法
    @Override
    public boolean delete(String[] ids) {
        //调用dao执行删除的方法
        //因为对数据库中的数据修改操作一定要提交
        //获得sqlsession
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityDao activityDao = session.getMapper(ActivityDao.class);
        ActivityRemarkDao activityRemarkDao = session.getMapper(ActivityRemarkDao.class);

        //设置一个标志位
        boolean flag = true;

        //调用dao中的方法进行删除操作
        //怎么能确定删除是否成功    应该删除的条数==受影响的条数
        //int countAll = activityDao.getCountByAids(ids);

        //查询出需要删除的备注的数量   因为备注和市场活动有关联关系
        int countAll = activityRemarkDao.getCountByAids(ids);
        //删除备注，返回受到影响的条数（实际删除的数量）
        int countRemarkModify = activityRemarkDao.deleteByAids(ids);
        if (countAll != countRemarkModify) {
            flag = false;
            session.rollback();
        }


        int countDeleteModify = activityDao.delete(ids);

        if (ids.length != countDeleteModify) {
            flag = false;
            session.rollback();
        }

        session.commit();
        session.close();
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //要返回map
        Map<String, Object> map = new HashMap<>();

        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityDao activityDao = session.getMapper(ActivityDao.class);
        UserDao userDao = session.getMapper(UserDao.class);

        //查询所有用户
        List<User> userList = userDao.getUserList();

        //查询所有信息根据id
        Activity activity = activityDao.getById(id);
        map.put("uList", userList);
        map.put("activity", activity);

        return map;
    }

    @Override
    public boolean update(Activity a) {

        boolean flag = true;
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityDao activityDao = session.getMapper(ActivityDao.class);
        int count = activityDao.update(a);
        if (count == 0) {
            flag = false;

            //暂时先这样写
            session.rollback();
        } else {

            //暂时先这样写
            session.commit();
        }
        //关闭session
        session.close();
        return flag;
    }

    @Override
    public Activity detail(String id) {
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityDao activityDao = session.getMapper(ActivityDao.class);


        Activity activity = activityDao.detail(id);


        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String id) {
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityRemarkDao activityRemarkDao = session.getMapper(ActivityRemarkDao.class);
        //查询不需提交
        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(id);


        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;

        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityRemarkDao activityRemarkDao = session.getMapper(ActivityRemarkDao.class);
        int count = activityRemarkDao.deleteRemark(id);
        if (count == 0) {
            flag = false;
            session.rollback();
        }
        session.commit();
        session.close();
        return flag;
    }

    //添加备注的方法
    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityRemarkDao activityRemarkDao = session.getMapper(ActivityRemarkDao.class);

        int count = activityRemarkDao.saveRemark(ar);
        if (count != 1) {
            flag = false;
            session.rollback();
        }

        session.commit();
        session.close();
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityRemarkDao activityRemarkDao = session.getMapper(ActivityRemarkDao.class);
        int count = activityRemarkDao.updateRemark(ar);
        if (count != 1) {
            flag = false;
            session.rollback();
        }

        session.commit();
        session.close();
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityDao activityDao =session.getMapper(ActivityDao.class);
        List<Activity> aList = activityDao.getActivityListByClueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        SqlSession session =SqlSessionUtil.getSqlSession();
        ActivityDao  activityDao =session.getMapper(ActivityDao.class);

        List<Activity>aList= activityDao.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        SqlSession session = SqlSessionUtil.getSqlSession();
        ActivityDao activityDao= session.getMapper(ActivityDao.class);

        List<Activity>aList= activityDao.getActivityListByName(aname);


        return aList;
    }
}
