package com.kang.jhipster.web.rest;

import com.kang.jhipster.security.AESUtils;
import com.kang.jhipster.security.RSAUtils;
import com.kang.jhipster.security.UUIDUtils;
import com.kang.jhipster.security.jwt.JWTConfigurer;
import com.kang.jhipster.security.jwt.TokenProvider;
import com.kang.jhipster.service.UserService;
import com.kang.jhipster.web.rest.vm.LoginVM;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final org.slf4j.Logger log = LoggerFactory.getLogger(UserJWTController.class);

    @Resource
    private CacheManager cacheManager;

    private String privateKeyStr =
        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ5wFglnCZaYGyBx" +
            "NfKQOlaHzNNZe/qE+HRLI4ivUJ4ldMfslUjRShxxBl3Xzw9UkH9OJngJaVeObrve" +
            "0MCJ46hg01deviYIC3S+9vvej9fT+HgO0yGhZ86s0iXEBB727tO6nig5HlloJHaZ" +
            "WZS7Z4cKhwcJoUgovP99SPSF7jVlAgMBAAECgYAEseGVSb5Y/gzfuzsUAv4XjlKd" +
            "eKtZ0xLhr1BQPpGu/gEl43bQ/5KooRjFMx3poGHfw9sT94NtmsThQEsDSZK7YKN8" +
            "08imAJiEeBx6ImwM7L4vm0QtxcuRpcz2J5gNzV33FnHMUC+nivU7RsX5M1VDfIbV" +
            "um7Kgo438fXWcHx+iQJBAMsNH9b5Rl/66gp3EhXEeGs33v6ASN2HGwIkJmbTnWyU" +
            "KvIsf9eKmq7grhqhTd4vfFZcswu1QH7+me9VGPGWZ3sCQQDHwMAx47+XYuwrznJ/" +
            "ef4E8qUAAYHD9uvoB2Eadakp4/VzK22XGfn2l5d0zNg5bwkmSBYhtZg6ICSskGkd" +
            "UNCfAkBnxTRRdCGwKZZ0dLfMYhU8jlgrbrpOZJ678Gejw2A/vlVYYL+REyfMWc0A" +
            "lRErjM8Zf9SNFjt463sWIkJWLQyDAkEAm6GE3Rn560Qqh9L4iHOOw2IdxkxmQz09" +
            "/fDJ6iikHTw7v3ilkOWvSD5BxcHX8Z+ePFJL1AW9TgQ/LqfxDeMqNwJAJUJ1J9qF" +
            "QiCRKfgolssqeXCpjKY/64MvvVlYYvV7SPlFSpzl8F7lzfjTXxtYWB9Mb1NUo+t1" +
            "GLfwbcRexF2Rrw==";

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * @param aesKeyStr
     * @return clientId
     */
    @PostMapping("/init")
    @Timed
    public ResponseEntity init(@Valid @RequestBody String aesKeyStr) {
        RSAPrivateKey privateKey= null;
        String aesKey = null;
        if(StringUtils.isEmpty(privateKeyStr)){
            return null;
        }
        try {
            System.out.println(aesKeyStr);
            RSAPrivateKey rsaPrivateKey = RSAUtils.loadPrivateKey(privateKeyStr);
            aesKey = RSAUtils.RSADecode(aesKeyStr, rsaPrivateKey);
            String uuid = UUIDUtils.getUUID();
            Cache cache = cacheManager.getCache("aesKey");
            cache.put(uuid, aesKey);
            Map map=new HashMap<>();
            map.put("clientId",uuid);
            return new ResponseEntity(map,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        //处理加密用户信息
        //  ---   sart ---
        Cache cache = cacheManager.getCache("aesKey");
        String useInfo = loginVM.getUseInfo();
        String aesKey=null;
        if(useInfo.indexOf("####")>0){
            String[] split = useInfo.split("####");
            String uuid=split[1];
            if(StringUtils.isNotEmpty(uuid)){
                aesKey =(String) cache.get(uuid).get();
                useInfo=split[0];
            }else{
                log.info("uuid 为空");
            }
        }else{
            log.info("useInfo 错误");
        }

        if(StringUtils.isNotEmpty(aesKey)){
            try {
                String data = AESUtils.aesDecrypt(useInfo,aesKey);
                data=data.replaceAll("\"","");
                if (data.indexOf("####") > 0) {
                    String[] split2 = data.split("####");
                    loginVM.setUsername(split2[0]);
                    loginVM.setPassword(split2[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            log.info("aesKey 为空");
        }
        //  ---   end ---

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
