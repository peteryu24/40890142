package com.skmns.codingtest.controller;

import com.skmns.codingtest.dto.AuthDTO;
import com.skmns.codingtest.service.AuthService;
import com.skmns.codingtest.util.SkmnsResult;
import com.skmns.codingtest.vo.AuthVO;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 인증 관련 API를 처리하는 컨트롤러 클래스입니다.
 * 회원가입, 로그인, 로그아웃, 아이디 중복 확인 기능을 제공합니다.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * AuthController 생성자
     * 
     * @param authService 인증 관련 서비스 클래스
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 사용자 아이디 중복 확인을 위한 API
     * 
     * <p>
     * 클라이언트가 제공한 사용자 이름에 대한 중복 여부를 확인하고,
     * 중복 여부를 반환하여 사용자에게 알려줍니다.
     * </p>
     *
     * @param authDTO 사용자 정보를 담고 있는 DTO 객체
     * @return 아이디 중복 여부를 나타내는 Boolean 값
     */
    @PostMapping("/check-username")
    public SkmnsResult<Boolean> checkUsername(@RequestBody AuthDTO authDTO) {
        boolean isUsernameAvailable = authService.isUsernameAvailable(authDTO.getUsername());
        return new SkmnsResult<>("아이디 중복 확인", HttpStatus.OK.value(), isUsernameAvailable);
    }

    /**
     * 사용자 회원가입을 처리하는 API
     * 
     * <p>
     * 회원가입을 진행하기 전에 아이디 중복 확인을 해야 하며, 중복이 아니어야 회원가입이 가능합니다.
     * 중복 확인을 하지 않고 회원가입을 시도하면 {@link IllegalArgumentException} 예외가 발생합니다.
     * </p>
     *
     * @param authDTO 회원가입 정보가 담긴 DTO 객체
     * @return 회원가입 성공 메시지와 함께 새로 등록된 사용자의 정보
     * @throws IllegalArgumentException 아이디 중복이 확인되지 않은 경우 예외 발생
     */
    @PostMapping("/register")
    public SkmnsResult<AuthDTO> register(@RequestBody AuthDTO authDTO) {

        if (authDTO.getIsUsernameAvailable()) {
            AuthVO registeredUser = authService.registerUser(authDTO.getUsername(), authDTO.getPassword());
            return new SkmnsResult<>("회원가입 성공", HttpStatus.CREATED.value(),
                    new AuthDTO(registeredUser.getUserId(), registeredUser.getUsername(), null));
        } else {
            throw new IllegalArgumentException("아이디 중복 확인을 먼저 해주세요.");
        }
    }

    /**
     * 사용자 로그인을 처리하는 API
     * 
     * <p>
     * 사용자가 제공한 로그인 정보(아이디, 비밀번호)를 기반으로 인증을 진행하며,
     * 인증 성공 시 HTTP 세션에 사용자 정보를 저장합니다.
     * </p>
     *
     * @param authDTO 로그인 정보가 담긴 DTO 객체
     * @param session HTTP 세션 객체
     * @return 로그인 성공 메시지와 함께 로그인된 사용자의 정보
     */
    @PostMapping("/login")
    public SkmnsResult<AuthDTO> login(@RequestBody AuthDTO authDTO, HttpSession session) {
        AuthVO authenticatedUser = authService.authenticate(authDTO.getUsername(), authDTO.getPassword());

        // SecurityContext에 인증 정보 설정
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser, null, List.of()); // 권한 추가 가능
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 세션에 사용자 정보 저장
        session.setAttribute("userId", authenticatedUser.getUserId());
        session.setAttribute("username", authenticatedUser.getUsername());

        return new SkmnsResult<>("로그인 성공", HttpStatus.OK.value(),
                new AuthDTO(authenticatedUser.getUserId(), authenticatedUser.getUsername(), null));
    }

    /**
     * 사용자 로그아웃을 처리하는 API
     * 
     * <p>
     * 로그인된 사용자의 세션을 무효화하여 로그아웃을 처리합니다.
     * </p>
     *
     * @param session HTTP 세션 객체
     * @return 로그아웃 성공 메시지
     */
    @PostMapping("/logout")
    public SkmnsResult<Void> logout(HttpSession session) {
        session.invalidate();
        return new SkmnsResult<>("로그아웃 성공", HttpStatus.OK.value(), null);
    }

    @GetMapping("/session-check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 존재하지 않습니다.");
        }
        return ResponseEntity.ok("세션 유지 중, 사용자 ID: " + userId);
    }

}
