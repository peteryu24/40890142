package com.skmns.codingtest.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skmns.codingtest.service.FileService;
import com.skmns.codingtest.util.SkmnsResult;

/**
 * 파일 관리 API를 처리하는 컨트롤러 클래스입니다.
 * 파일 다운로드, 추가, 수정 기능을 제공합니다.
 */
@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    /**
     * FileController 생성자
     * 
     * @param fileService 파일 관련 서비스 클래스
     */
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 게시글에 파일을 추가하는 API
     * 
     * <p>
     * 주어진 게시글 ID에 파일을 추가하는 기능을 제공합니다.
     * </p>
     * 
     * @param articleId 파일을 추가할 게시글의 ID
     * @param file      업로드할 파일
     * @return 파일 추가 성공 메시지
     * @throws IOException 파일 업로드 중 오류 발생 시 예외
     */
    @PostMapping("/{articleId}/add")
    public SkmnsResult<Void> addFileToArticle(
            @PathVariable Long articleId,
            @RequestParam("file") MultipartFile file) throws IOException {

        fileService.addFileToArticle(articleId, file);
        return new SkmnsResult<>("파일이 추가되었습니다.", HttpStatus.CREATED.value());
    }
}
