package com.skmns.codingtest.dto;

import java.time.LocalDateTime;

public class ArticleDTO {
    private Long articleId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int viewCount;
    private boolean hasFile;
    private String authorUsername;

    public ArticleDTO(Long articleId, String title, String content, LocalDateTime createdAt, int viewCount,
            boolean hasFile, String authorUsername) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.hasFile = hasFile;
        this.authorUsername = authorUsername;
    }

    // setter의 사용 X :: 데이터 불변성 보장

    public Long getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getViewCount() {
        return viewCount;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }
}
