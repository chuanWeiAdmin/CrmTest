package com.mytest.crm.workbench.dao;

import com.mytest.crm.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);
}
