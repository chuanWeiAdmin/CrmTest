package com.mytest.crm.workbench.service.impl;

import com.mytest.crm.utils.SqlSessionUtil;
import com.mytest.crm.workbench.dao.ClueActivityRelationDao;
import com.mytest.crm.workbench.dao.ClueDao;
import com.mytest.crm.workbench.domain.Clue;
import com.mytest.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSession;

public class ClueServiceImpl implements ClueService {
    @Override
    public boolean save(Clue c) {
        SqlSession session = SqlSessionUtil.getSqlSession();
        ClueDao clueDao = session.getMapper(ClueDao.class);
        boolean flag = true;
        int count = clueDao.save(c);
        if (count != 1) {
            flag = false;
            session.rollback();
        }
        session.commit();
        session.close();
        return flag;
    }

    @Override
    public Clue detail(String id) {

        SqlSession session = SqlSessionUtil.getSqlSession();
        ClueDao clueDao = session.getMapper(ClueDao.class);
        Clue clue = clueDao.detail(id);

        return clue;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        SqlSession session = SqlSessionUtil.getSqlSession();
        ClueActivityRelationDao clueActivityRelationDao = session.getMapper(ClueActivityRelationDao.class);
        int count = clueActivityRelationDao.unbund(id);
        if (count != 1) {
            flag = false;
            session.rollback();
        }
        session.commit();
        session.close();
        return flag;
    }
}
