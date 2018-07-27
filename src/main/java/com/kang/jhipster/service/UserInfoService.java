package com.kang.jhipster.service;

import com.kang.jhipster.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    public UserInfo get(long id);

    public List<UserInfo> getAll();
}
