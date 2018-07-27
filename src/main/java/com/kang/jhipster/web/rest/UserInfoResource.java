package com.kang.jhipster.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.kang.jhipster.entity.UserInfo;
import com.kang.jhipster.service.UserInfoService;
import com.kang.jhipster.service.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class UserInfoResource {
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("getAll")
    public String getAll() throws IOException{
        List<UserInfo> userInfos = userInfoService.getAll();
        List<UserDTO> userDtos=new ArrayList<>();
        for (UserInfo user :userInfos
            ) {
            UserDTO userDto=new UserDTO();
            BeanUtils.copyProperties(user,userDto);
            userDtos.add(userDto);
        }
        return new ObjectMapper().writeValueAsString(userDtos);
    }

    @RequestMapping("getUser")
    public String get() throws IOException {
        Long id=1L;
        UserInfo userInfo = userInfoService.get(id);
        System.out.println(userInfo);
        UserDTO userDto=new UserDTO();
        BeanUtils.copyProperties(userInfo,userDto);
        return new ObjectMapper().writeValueAsString(userDto);
    }


}

