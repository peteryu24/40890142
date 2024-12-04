package com.skmns.codingtest.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUploadUtil {

    // @Value 어노테이션을 사용하여 file.upload-dir 값을 읽어옵니다.
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 파일 저장
     */
    public String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        // 업로드 디렉토리가 없으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    /**
     * 파일 로드
     */
    public byte[] loadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new java.io.FileNotFoundException("파일을 찾을 수 없습니다: " + filePath);
        }

        return Files.readAllBytes(path);
    }
}
