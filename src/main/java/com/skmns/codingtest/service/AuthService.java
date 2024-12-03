package com.skmns.codingtest.service;

import com.skmns.codingtest.entity.AuthEntity;
import com.skmns.codingtest.repository.AuthRepository;
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

    public AuthEntity registerUser(String username, String password) {
        if (authRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        AuthEntity newUser = new AuthEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));

        return authRepository.save(newUser);
    }

    public AuthEntity authenticate(String username, String password) {
        AuthEntity user = authRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SecurityException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}
