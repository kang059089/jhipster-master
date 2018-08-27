package com.kang.jhipster.service.util;

import java.util.UUID;

/**
 * 生成UUID的工具类
 */
public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
