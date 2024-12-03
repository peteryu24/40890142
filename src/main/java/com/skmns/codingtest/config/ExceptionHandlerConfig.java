package com.skmns.codingtest.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.skmns.codingtest.util.SkmnsResult;

@ControllerAdvice
public class ExceptionHandlerConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    /**
     * 모든 예외 처리 (Generic Exception)
     */
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<SkmnsResult<String>> handleGenericException(Exception e) {
        logger.error("서버 에러 발생: {}", e.getMessage(), e);
        SkmnsResult<String> result = new SkmnsResult<>(
                "서버 에러 발생",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(value = { org.springframework.security.core.AuthenticationException.class })
    public ResponseEntity<SkmnsResult<String>> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException e) {
        logger.warn("인증 실패: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "인증이 필요합니다.",
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    @ExceptionHandler(value = { org.springframework.security.access.AccessDeniedException.class })
    public ResponseEntity<SkmnsResult<String>> handleAccessDeniedException(
            org.springframework.security.access.AccessDeniedException e) {
        logger.warn("권한이 없습니다: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "권한이 없습니다.",
                HttpStatus.FORBIDDEN.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    @ExceptionHandler(value = { java.util.NoSuchElementException.class })
    public ResponseEntity<SkmnsResult<String>> handleNoSuchElementException(java.util.NoSuchElementException e) {
        logger.warn("리소스를 찾을 수 없습니다: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "요청한 리소스를 찾을 수 없습니다.",
                HttpStatus.NOT_FOUND.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<SkmnsResult<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("잘못된 요청: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "잘못된 요청입니다.",
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = { SecurityException.class })
    public ResponseEntity<SkmnsResult<String>> handleSecurityException(SecurityException e) {
        logger.warn("보안 문제 발생: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "권한이 없습니다.",
                HttpStatus.FORBIDDEN.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    @ExceptionHandler(value = { java.io.FileNotFoundException.class })
    public ResponseEntity<SkmnsResult<String>> handleFileNotFoundException(java.io.FileNotFoundException e) {
        logger.warn("파일을 찾을 수 없음: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "파일을 찾을 수 없습니다.",
                HttpStatus.NOT_FOUND.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(value = { IOException.class })
    public ResponseEntity<SkmnsResult<String>> handleIOException(IOException e) {
        logger.error("파일 처리 오류 발생: {}", e.getMessage(), e);
        SkmnsResult<String> result = new SkmnsResult<>(
                "파일 처리 중 오류가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(value = { org.springframework.web.bind.MethodArgumentNotValidException.class })
    public ResponseEntity<SkmnsResult<String>> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        logger.warn("유효성 검사 실패: {}", errorMessage);
        SkmnsResult<String> result = new SkmnsResult<>(
                "유효성 검사 실패: " + errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = { org.springframework.web.bind.MissingServletRequestParameterException.class })
    public ResponseEntity<SkmnsResult<String>> handleMissingParameterException(
            org.springframework.web.bind.MissingServletRequestParameterException e) {
        logger.warn("필수 요청 파라미터 누락: {}", e.getParameterName());
        SkmnsResult<String> result = new SkmnsResult<>(
                "필수 요청 파라미터가 누락되었습니다: " + e.getParameterName(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = { org.springframework.dao.DataIntegrityViolationException.class })
    public ResponseEntity<SkmnsResult<String>> handleDataIntegrityViolationException(
            org.springframework.dao.DataIntegrityViolationException e) {
        logger.error("데이터 무결성 위반 발생: {}", e.getMessage(), e);
        SkmnsResult<String> result = new SkmnsResult<>(
                "데이터 무결성 위반 오류가 발생했습니다.",
                HttpStatus.CONFLICT.value(),
                e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @ExceptionHandler(value = { org.springframework.web.HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<SkmnsResult<String>> handleMethodNotSupportedException(
            org.springframework.web.HttpRequestMethodNotSupportedException e) {
        logger.warn("HTTP 메서드 지원되지 않음: {}", e.getMethod());
        SkmnsResult<String> result = new SkmnsResult<>(
                "HTTP 메서드가 지원되지 않습니다: " + e.getMethod(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }

    @ExceptionHandler(value = { org.springframework.web.multipart.MaxUploadSizeExceededException.class })
    public ResponseEntity<SkmnsResult<String>> handleMaxUploadSizeExceededException(
            org.springframework.web.multipart.MaxUploadSizeExceededException e) {
        logger.warn("파일 크기 초과: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "업로드 가능한 파일 크기를 초과했습니다.",
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(result);
    }

    @ExceptionHandler(value = { org.springframework.web.multipart.MultipartException.class })
    public ResponseEntity<SkmnsResult<String>> handleMultipartException(
            org.springframework.web.multipart.MultipartException e) {
        logger.warn("파일 업로드 오류: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "파일 업로드 중 오류가 발생했습니다.",
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = { com.fasterxml.jackson.core.JsonParseException.class })
    public ResponseEntity<SkmnsResult<String>> handleJsonParseException(
            com.fasterxml.jackson.core.JsonParseException e) {
        logger.warn("JSON 파싱 오류: {}", e.getMessage());
        SkmnsResult<String> result = new SkmnsResult<>(
                "잘못된 JSON 형식입니다.",
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = { NullPointerException.class })
    public ResponseEntity<SkmnsResult<String>> handleNullPointerException(NullPointerException e) {
        logger.error("NullPointerException 발생: {}", e.getMessage(), e);
        SkmnsResult<String> result = new SkmnsResult<>(
                "서버 내부 오류가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
