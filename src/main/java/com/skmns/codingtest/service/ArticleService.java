package com.skmns.codingtest.service;

import com.skmns.codingtest.dto.ArticleDTO;
import com.skmns.codingtest.entity.ArticleEntity;
import com.skmns.codingtest.entity.FileEntity;
import com.skmns.codingtest.repository.ArticleRepository;
import com.skmns.codingtest.repository.FileRepository;
import com.skmns.codingtest.util.PaginationUtil;
import com.skmns.codingtest.vo.ArticleVO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final FileRepository fileRepository;

    public ArticleService(ArticleRepository articleRepository, FileRepository fileRepository) {
        this.articleRepository = articleRepository;
        this.fileRepository = fileRepository;
    }

    public List<ArticleVO> getArticleList() {
        return articleRepository.findAll().stream()
                .map(article -> new ArticleVO(
                        article.getArticleId(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCreatedAt(),
                        article.getViewCount(),
                        article.getAuthor().getUsername(),
                        article.isHasFile()))
                .toList();
    }

    public ArticleVO getArticleWithFiles(Long articleId) {
        ArticleEntity article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        List<FileEntity> files = article.isHasFile() ? fileRepository.findByArticleId(articleId) : List.of();

        return new ArticleVO(
                article.getArticleId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getViewCount(),
                article.getAuthor().getUsername(),
                article.isHasFile());
    }

    public PaginationUtil<ArticleVO> getArticles(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ArticleEntity> articlePage = articleRepository.findAll(pageRequest);

        List<ArticleVO> articles = articlePage.getContent().stream()
                .map(article -> new ArticleVO(
                        article.getArticleId(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCreatedAt(),
                        article.getViewCount(),
                        article.getAuthor().getUsername(),
                        article.isHasFile()))
                .collect(Collectors.toList());

        return new PaginationUtil<>(articles, articlePage.getTotalPages(), articlePage.getNumber(),
                articlePage.getTotalElements());
    }

}
