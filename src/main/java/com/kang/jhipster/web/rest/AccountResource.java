package com.kang.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;

import com.kang.jhipster.domain.User;
import com.kang.jhipster.repository.UserRepository;
import com.kang.jhipster.security.SecurityUtils;
import com.kang.jhipster.service.MailService;
import com.kang.jhipster.service.UserService;
import com.kang.jhipster.service.dto.UserDTO;
import com.kang.jhipster.web.rest.errors.*;
import com.kang.jhipster.web.rest.vm.KeyAndPasswordVM;
import com.kang.jhipster.web.rest.vm.ManagedUserVM;

import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import com.kang.jhipster.service.dto.PasswordChangeDTO;
import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Resource
    private CacheManager cacheManager;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * POST  /register : register the user.
     * 用户注册
     * @param managedUserVM the managed user View Model
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already used
     * @return 返回值为0：重置成功；为1：重置失败；为2：验证码错误
     */
    @PostMapping("/register")
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public Integer registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        //获取identifyingCode的缓存条目
        Cache identifyingCodeCache = cacheManager.getCache("identifyingCode");
        try {
            //获取存储到缓存中的code验证码
            String code = (String)identifyingCodeCache.get(managedUserVM.getClientId()).get();
            if (code.equals(managedUserVM.getCode()) || code == managedUserVM.getCode()) {
                managedUserVM.setActivated(true);
                User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
                //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(user));
                return 0;
            }
            return 2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(new User()));
        return 1;
//        if (!checkPasswordLength(managedUserVM.getPassword())) {
//            throw new InvalidPasswordException();
//        }
        //userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(u -> {throw new LoginAlreadyUsedException();});
        //userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(u -> {throw new EmailAlreadyUsedException();});
        //User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        //mailService.sendActivationEmail(user);
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    @Timed
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this activation key");
        }
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Timed
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws RuntimeException 500 (Internal Server Error) if the user login wasn't found
     */
    @PostMapping("/account")
    @Timed
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
   }

    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param passwordChangeDto current and new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    @Timed
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
   }

    /**
     * POST   /account/reset-password/init : Send an email to reset the password of the user
     *
     * @param mail the mail of the user
     * @throws EmailNotFoundException 400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = "/account/reset-password/init")
    @Timed
    public void requestPasswordReset(@RequestBody String mail) {
       mailService.sendPasswordResetMail(
           userService.requestPasswordReset(mail)
               .orElseThrow(EmailNotFoundException::new)
       );
    }

    /**
     * POST   /account/reset-password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws RuntimeException 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset-password/finish")
    @Timed
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
    }

    /**
     * 用户重置密码
     * @param managedUserVM
     */
    /**
     * 用户重置密码
     * @param managedUserVM
     * @return 返回值为0：重置成功；为1：重置失败；为2：验证码错误
     */
    @PostMapping("/resetPassword")
    @Timed
    public Integer resetPassword(@Valid @RequestBody ManagedUserVM managedUserVM) {
        log.debug("重置密码请求参数为 : {}", managedUserVM);
        //获取identifyingCode的缓存条目
        Cache identifyingCodeCache = cacheManager.getCache("identifyingCode");
        try {
            //获取存储到缓存中的code验证码
            String code = (String)identifyingCodeCache.get(managedUserVM.getClientId()).get();
            if (code.equals(managedUserVM.getCode()) || code == managedUserVM.getCode()) {
                User user = new User();
                if (!"".equals(managedUserVM.getEmail()) || "" != managedUserVM.getEmail()) {
                    user = userRepository.getUserByEmail(managedUserVM.getEmail());

                } else if (!"".equals(managedUserVM.getPhone()) || "" != managedUserVM.getPhone()) {
                    user = userRepository.getUserByPhone(managedUserVM.getPhone());
                }
                user.setPassword(passwordEncoder.encode(managedUserVM.getPassword()));
                userRepository.save(user);
                Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
//                userRepository.save(user);
                //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(user));
                return 0;
            }
            return 2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(new User()));
        return 1;
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
