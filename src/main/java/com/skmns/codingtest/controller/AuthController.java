package com.skmns.codingtest.controller;

import com.skmns.codingtest.dto.AuthDTO;
import com.skmns.codingtest.service.AuthService;
import com.skmns.codingtest.util.SkmnsResult;
import com.skmns.codingtest.vo.AuthVO;

import jakarta.servlet.http.HttpSession;

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
    public SkmnsResult<AuthDTO> register(@RequestBody AuthDTO authDTO) {
        AuthVO registeredUser = authService.registerUser(authDTO.getUsername(), authDTO.getPassword());
        return new SkmnsResult<>("회원가입 성공", HttpStatus.CREATED.value(),
                new AuthDTO(registeredUser.getUserId(), registeredUser.getUsername(), null));
    }

    @PostMapping("/login")
    public SkmnsResult<AuthDTO> login(@RequestBody AuthDTO authDTO, HttpSession session) {
        AuthVO authenticatedUser = authService.authenticate(authDTO.getUsername(), authDTO.getPassword());

        // 세션에 사용자 정보 저장
        session.setAttribute("userId", authenticatedUser.getUserId());
        session.setAttribute("username", authenticatedUser.getUsername());

        return new SkmnsResult<>("로그인 성공", HttpStatus.OK.value(),
                new AuthDTO(authenticatedUser.getUserId(), authenticatedUser.getUsername(), null));
    }

    @PostMapping("/logout")
    public SkmnsResult<Void> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return new SkmnsResult<>("로그아웃 성공", HttpStatus.OK.value(), null);
    }
}
