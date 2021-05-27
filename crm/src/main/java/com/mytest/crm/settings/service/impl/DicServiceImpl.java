package com.mytest.crm.settings.service.impl;

import com.mytest.crm.settings.dao.DicTypeDao;
import com.mytest.crm.settings.dao.DicValueDao;
import com.mytest.crm.settings.domain.DicType;
import com.mytest.crm.settings.domain.DicValue;
import com.mytest.crm.settings.service.DicService;
import com.mytest.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();

        SqlSession session = SqlSessionUtil.getSqlSession();
        DicTypeDao dicTypeDao = session.getMapper(DicTypeDao.class);
        DicValueDao dicValueDao = session.getMapper(DicValueDao.class);

        //先取类型
        List<DicType> dtList = dicTypeDao.getTypeList();

        for (DicType dt : dtList) {
            //根据类型查询value放到type中
            String code = dt.getCode();
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code,dvList);
        }

        return map;
    }
}
