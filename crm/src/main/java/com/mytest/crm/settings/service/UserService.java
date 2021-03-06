package com.mytest.crm.settings.service;

import com.mytest.crm.exception.LoginException;
import com.mytest.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
