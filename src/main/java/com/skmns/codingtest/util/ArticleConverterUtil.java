package com.skmns.codingtest.util;

import com.skmns.codingtest.dto.ArticleDTO;
import com.skmns.codingtest.entity.ArticleEntity;
import com.skmns.codingtest.vo.ArticleVO;

public class ArticleConverterUtil {

    // VO -> DTO 변환
    public static ArticleDTO toDTO(ArticleVO articleVO) {
        return new ArticleDTO(
                articleVO.getArticleId(),
                articleVO.getTitle(),
                articleVO.getContent(),
                articleVO.getCreatedAt(),
                articleVO.getViewCount(),
                articleVO.isHasFile(),
                articleVO.getAuthorUsername(),
                articleVO.getFileNames());
    }

    // Entity -> VO 변환
    public static ArticleVO toVO(ArticleEntity article) {
        return new ArticleVO(
                article.getArticleId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getViewCount(),
                article.getAuthor().getUsername(),
                article.isHasFile());
    }
}
