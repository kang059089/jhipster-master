package com.kang.jhipster.verificationcode;

import com.codahale.metrics.annotation.Timed;
import com.kang.jhipster.config.Constants;
import com.kang.jhipster.domain.User;
import com.kang.jhipster.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class VerificationCodeResource {

    private final Logger log = LoggerFactory.getLogger(VerificationCodeResource.class);

    private final MailService mailService;

    @Resource
    private CacheManager cacheManager;



    public VerificationCodeResource (MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * 接收客户端的请求参数，生成验证码
     * @param phoneOrEmail 手机号码或邮箱
     * @param state 判断phoneOrEmail是手机还是邮箱（0：手机号码，1：邮箱）
     * @return
     */
    @GetMapping("/verify-code/send/{phoneOrEmail}/{state}/{clientId}")
    @Timed
    public String createVerificationCode(@PathVariable String phoneOrEmail, @PathVariable Integer state, @PathVariable String clientId) {
        log.debug("获取验证码请求参数为 : {}, {}", phoneOrEmail, state);

        Cache identifyingCodeCache = cacheManager.getCache("identifyingCode");

        //生成6位数的随机数字作为验证码
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code += random.nextInt(10);
        }
        identifyingCodeCache.put(clientId,code);

        System.out.println(identifyingCodeCache.get(clientId).get());

        //假如state为1，则向邮箱发送验证码；state为0，则向手机号码发送验证码。
        if(state == 1) {
            User user = new User();
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
            user.setEmail(phoneOrEmail);
            user.setCode(code);
            this.mailService.sendVerificationCodeMail(user);
        }
        return code;
    }
}
