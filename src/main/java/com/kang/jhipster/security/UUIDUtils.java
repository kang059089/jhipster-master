package com.kang.jhipster.security;

import java.util.UUID;

public final class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
