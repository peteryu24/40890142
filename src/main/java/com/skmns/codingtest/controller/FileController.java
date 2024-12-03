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
     * 파일 다운로드 API
     * 
     * <p>
     * 주어진 파일 ID에 해당하는 파일을 다운로드하는 기능을 제공합니다.
     * </p>
     * 
     * @param fileId 다운로드할 파일의 ID
     * @return 다운로드된 파일의 데이터
     */
    @GetMapping("/{fileId}/download")
    public SkmnsResult<byte[]> downloadFile(@PathVariable Long fileId) {

        byte[] fileData = fileService.downloadFile(fileId).getBody();
        return new SkmnsResult<>("파일 다운로드 성공", HttpStatus.OK.value(), fileData);
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

    /**
     * 파일을 수정하는 API
     * 
     * <p>
     * 주어진 파일 ID에 해당하는 파일을 수정하는 기능을 제공합니다.
     * </p>
     * 
     * @param fileId  수정할 파일의 ID
     * @param newFile 새로운 파일 데이터
     * @return 파일 수정 성공 메시지
     * @throws IOException 파일 수정 중 오류 발생 시 예외
     */
    @PutMapping("/{fileId}")
    public SkmnsResult<Void> updateFile(
            @PathVariable Long fileId,
            @RequestParam("file") MultipartFile newFile) throws IOException {

        fileService.updateFile(fileId, newFile);
        return new SkmnsResult<>("파일이 수정되었습니다.", HttpStatus.OK.value());
    }
}
