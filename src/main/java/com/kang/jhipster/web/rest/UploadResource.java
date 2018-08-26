package com.kang.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kang.jhipster.service.util.PictureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UploadResource {

    private final Logger log = LoggerFactory.getLogger(UploadResource.class);

    @Value("${imgFlogder}")
    private String imgFlogder;

    @Value("${avatarServer}")
    private String avatarServer;


    /**
     * 接收app上传的头像图片
     * @param avatarUpload 上传的头像图片参数
     * @return 返回保存的头像图片地址
     */
    @PostMapping("/avatar")
    @Timed
    public String avatarUpload(MultipartFile avatarUpload, @RequestParam("userId") String userId) {

        log.debug("从app接收到的头像图片参数为 : {}, 接收的参数为：{}", avatarUpload.getOriginalFilename(), userId);
        //判断头像图片是否为空
        if (!avatarUpload.isEmpty()) {
            //接收的头像图片名
            String fileName = avatarUpload.getOriginalFilename();
            //生成的头像图片保存路径
            String imgFilePath = avatarServer + PictureUtil.makeImgPath(avatarUpload, imgFlogder, "avatar", userId, fileName);
            log.debug("生成的头像图片保存路径: {}", imgFilePath);
            return imgFilePath;
        }
        return "";
    }
}
