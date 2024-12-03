package com.skmns.codingtest.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class FileDTO {
    private List<MultipartFile> files;

    public List<MultipartFile> getFiles() {
        return files;
    }
}
