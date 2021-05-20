package com.mytest.crm.workbench.dao;


import com.mytest.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String id);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}
