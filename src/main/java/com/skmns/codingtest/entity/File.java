package com.skmns.codingtest.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "files")
public class File {

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;

    @Column(name = "file_path", length = 512, nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article; // 게시글과 연관 관계 설정

    public File() {
    }

    public File(Long fileId, String fileName, String filePath, Article article) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.article = article;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
