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

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 파일 다운로드
     */
    @GetMapping("/{fileId}/download")
    public SkmnsResult<byte[]> downloadFile(@PathVariable Long fileId) {
        byte[] fileData = fileService.downloadFile(fileId).getBody();
        return new SkmnsResult<>("파일 다운로드 성공", HttpStatus.OK.value(), fileData);
    }

    /**
     * 파일 추가
     */
    @PostMapping("/{articleId}/add")
    public SkmnsResult<Void> addFileToArticle(
            @PathVariable Long articleId,
            @RequestParam("file") MultipartFile file) throws IOException {
        fileService.addFileToArticle(articleId, file);
        return new SkmnsResult<>("파일이 추가되었습니다.", HttpStatus.CREATED.value());
    }

    /**
     * 파일 수정
     */
    @PutMapping("/{fileId}")
    public SkmnsResult<Void> updateFile(
            @PathVariable Long fileId,
            @RequestParam("file") MultipartFile newFile) throws IOException {
        fileService.updateFile(fileId, newFile);
        return new SkmnsResult<>("파일이 수정되었습니다.", HttpStatus.OK.value());
    }
}
