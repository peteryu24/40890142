package com.skmns.codingtest.service;

import com.skmns.codingtest.entity.ArticleEntity;
import com.skmns.codingtest.entity.FileEntity;
import com.skmns.codingtest.repository.ArticleRepository;
import com.skmns.codingtest.repository.FileRepository;
import com.skmns.codingtest.util.FileUploadUtil;
import com.skmns.codingtest.vo.FileVO;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileUploadUtil fileUploadUtil;
    private final ArticleRepository articleRepository;

    public FileService(FileRepository fileRepository, FileUploadUtil fileUploadUtil,
            ArticleRepository articleRepository) {
        this.fileRepository = fileRepository;
        this.fileUploadUtil = fileUploadUtil;
        this.articleRepository = articleRepository;
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

    public void updateFile(Long fileId, MultipartFile newFile) {
        try {
            FileEntity existingFile = fileRepository.findById(fileId)
                    .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));

            fileUploadUtil.deleteFile(existingFile.getFileUrl()); // 기존 파일 삭제
            String newFilePath = fileUploadUtil.saveFile(newFile); // 새 파일 저장

            existingFile.setFileName(newFile.getOriginalFilename());
            existingFile.setFileUrl(newFilePath);
            fileRepository.save(existingFile); // 업데이트된 파일 정보 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 수정 중 오류 발생", e);
        }
    }

    public void deleteFilesByIds(List<Long> fileIds) {
        List<FileEntity> filesToDelete = fileRepository.findAllById(fileIds);
        filesToDelete.forEach(file -> {
            try {
                fileUploadUtil.deleteFile(file.getFileUrl());
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제 실패: " + file.getFileUrl(), e);
            }
        });
        fileRepository.deleteAll(filesToDelete);
    }

    public void attachFilesToArticle(List<MultipartFile> files, ArticleEntity article) throws IOException {
        List<FileEntity> fileEntities = saveFiles(files);
        fileEntities.forEach(file -> file.setArticle(article));
        fileRepository.saveAll(fileEntities);
    }

    public void deleteFilesByArticle(ArticleEntity article) {
        List<FileEntity> files = fileRepository.findByArticle_ArticleId(article.getArticleId());
        files.forEach(file -> {
            try {
                fileUploadUtil.deleteFile(file.getFileUrl());
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제 실패: " + file.getFileUrl(), e);
            }
        });
        fileRepository.deleteAll(files);
    }

    public void addFileToArticle(Long articleId, MultipartFile file) throws IOException {
        ArticleEntity article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        FileEntity fileEntity = new FileEntity();
        String filePath = fileUploadUtil.saveFile(file);
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileUrl(filePath);
        fileEntity.setArticle(article);

        fileRepository.save(fileEntity);
    }

    public ResponseEntity<byte[]> downloadFile(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));
        byte[] fileData = loadFile(fileEntity.getFileUrl());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .body(fileData);
    }

    private byte[] loadFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new IOException("파일을 찾을 수 없습니다: " + filePath);
            }
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("파일 로드 실패", e);
        }
    }
}
