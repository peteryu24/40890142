package com.skmns.codingtest.service;

import com.skmns.codingtest.entity.ArticleEntity;
import com.skmns.codingtest.entity.FileEntity;
import com.skmns.codingtest.repository.FileRepository;
import com.skmns.codingtest.util.FileUploadUtil;
import com.skmns.codingtest.vo.FileVO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileUploadUtil fileUploadUtil;

    public FileService(FileRepository fileRepository, FileUploadUtil fileUploadUtil) {
        this.fileRepository = fileRepository;
        this.fileUploadUtil = fileUploadUtil;
    }

    /**
     * 파일 업로드 및 FileEntity 생성
     */
    public List<FileEntity> saveFiles(List<MultipartFile> multipartFiles) throws IOException {
        return multipartFiles.stream()
                .map(file -> {
                    try {
                        String filePath = fileUploadUtil.saveFile(file); // 파일 저장
                        FileEntity fileEntity = new FileEntity();
                        fileEntity.setFileName(file.getOriginalFilename());
                        fileEntity.setFileUrl(filePath);
                        return fileEntity;
                    } catch (IOException e) {
                        throw new RuntimeException("파일 저장 실패", e);
                    }
                })
                .toList();
    }

    /**
     * 특정 게시글에 대한 파일 조회
     */
    public List<FileVO> getFilesByArticleId(Long articleId) {
        return fileRepository.findByArticle_ArticleId(articleId).stream()
                .map(file -> new FileVO(file.getFileName(), file.getFileUrl()))
                .toList();
    }

    public void updateFilesForArticle(List<MultipartFile> files, ArticleEntity article) throws IOException {
        // 기존 파일 삭제
        fileRepository.deleteAllByArticle(article);

        // 새로운 파일 저장
        attachFilesToArticle(files, article);
    }

    // 특정 파일 ID만 삭제
    public void deleteFilesByIds(List<Long> fileIds) {
        List<FileEntity> filesToDelete = fileRepository.findAllById(fileIds);
        filesToDelete.forEach(file -> fileUploadUtil.deleteFile(file.getFileUrl()));
        fileRepository.deleteAll(filesToDelete);
    }

    // 새 파일 추가
    public void attachFilesToArticle(List<MultipartFile> files, ArticleEntity article) throws IOException {
        List<FileEntity> fileEntities = saveFiles(files);
        fileEntities.forEach(file -> file.setArticle(article));
        fileRepository.saveAll(fileEntities);
    }

    public void deleteFilesByArticle(ArticleEntity article) {
        List<FileEntity> files = fileRepository.findByArticle_ArticleId(article.getArticleId());
        files.forEach(file -> fileUploadUtil.deleteFile(file.getFileUrl()));
        fileRepository.deleteAll(files);
    }
}
