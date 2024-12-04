package com.skmns.codingtest.service;

import com.skmns.codingtest.entity.AuthEntity;
import com.skmns.codingtest.repository.AuthRepository;
import com.skmns.codingtest.vo.AuthVO;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUsernameAvailable(String username) {
        return !authRepository.findByUsername(username).isPresent();
    }

    public AuthVO registerUser(String username, String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 null일 수 없습니다.");
        }

        if (authRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        AuthEntity newUser = new AuthEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));

        AuthEntity savedUser = authRepository.save(newUser);
        return new AuthVO(savedUser.getUserId(), savedUser.getUsername());
    }

    public AuthEntity authenticate(String username, String password) {

        AuthEntity authEntity = authRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, authEntity.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return authEntity;
    }

    public boolean isOwner(HttpSession session, Long resourceOwnerId) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new SecurityException("로그인이 필요합니다.");
        }

        return userId.equals(resourceOwnerId);
    }
}
