package com.skmns.codingtest.controller;

import com.skmns.codingtest.entity.UserEntity;
import com.skmns.codingtest.service.AuthService;
import com.skmns.codingtest.util.SkmnsResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public SkmnsResult<UserEntity> register(@RequestParam String username, @RequestParam String password) {
        UserEntity registeredUser = authService.registerUser(username, password);
        return new SkmnsResult<>("회원가입 성공", HttpStatus.CREATED.value(), registeredUser);
    }

    @PostMapping("/login")
    public SkmnsResult<UserEntity> login(@RequestParam String username, @RequestParam String password) {
        UserEntity authenticatedUser = authService.authenticate(username, password);
        return new SkmnsResult<>("로그인 성공", HttpStatus.OK.value(), authenticatedUser);
    }
}
