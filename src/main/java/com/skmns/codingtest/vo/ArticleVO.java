package com.skmns.codingtest.vo;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleVO {
    private Long articleId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int viewCount;
    private String authorUsername;
    private boolean hasFile; // 파일 유무만

    public ArticleVO(Long articleId, String title, String content, LocalDateTime createdAt, int viewCount,
            String authorUsername, boolean hasFile) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.authorUsername = authorUsername;
        this.hasFile = hasFile;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }
}
