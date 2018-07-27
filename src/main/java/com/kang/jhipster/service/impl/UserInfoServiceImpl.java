package com.kang.jhipster.service.impl;

import com.kang.jhipster.dao.UserInfoMapper;
import com.kang.jhipster.entity.UserInfo;
import com.kang.jhipster.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userDao;

    @Override
    public UserInfo get(long id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public List<UserInfo> getAll() {
        return userDao.getAllUsers();
    }
}

