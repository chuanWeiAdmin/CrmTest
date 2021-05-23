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
        //两个dao
        SqlSession session = SqlSessionUtil.getSqlSession();
        DicTypeDao dicTypeDao = session.getMapper(DicTypeDao.class);
        DicValueDao dicValueDao = session.getMapper(DicValueDao.class);

        //先要获得所有的DicType
        //再获得所有的DicValue

        //1.获得所有的DicType
        List<DicType> typeList = dicTypeDao.getTypeList();

        Map<String, List<DicValue>> map = new HashMap<>();
        //2.遍历List 通过类型分类取数据
        for (DicType dt : typeList) {
            //通过类型对象获得code
            String code = dt.getCode();
            List<DicValue> valueList = dicValueDao.getListByCode(code);
            map.put(code, valueList);
        }
        return map;
    }
}
