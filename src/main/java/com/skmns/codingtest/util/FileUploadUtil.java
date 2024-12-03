package com.skmns.codingtest.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUploadUtil {

    private static final String UPLOAD_DIR = "C:/upload/";

    /**
     * 파일 저장
     */
    public String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        // 디렉토리 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String fileUrl) {
        File file = new File(fileUrl);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("파일 삭제 실패: " + fileUrl);
            }
        }
    }
}
