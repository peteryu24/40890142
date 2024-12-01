package com.skmns.codingtest.controller;

import com.skmns.codingtest.dto.ArticleDTO;
import com.skmns.codingtest.service.ArticleService;
import com.skmns.codingtest.vo.ArticleVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleDTO> getArticles() {
        List<ArticleVO> articleVOList = articleService.getArticleList();
        return articleVOList.stream()
                .map(articleVO -> new ArticleDTO(
                        articleVO.getArticleId(),
                        articleVO.getTitle(),
                        articleVO.getContent(),
                        articleVO.getCreatedAt(),
                        articleVO.getViewCount(),
                        articleVO.isHasFile()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ArticleDTO getArticle(@PathVariable Long id) {
        ArticleVO articleVO = articleService.getArticleWithFiles(id);
        return new ArticleDTO(
                articleVO.getArticleId(),
                articleVO.getTitle(),
                articleVO.getContent(),
                articleVO.getCreatedAt(),
                articleVO.getViewCount(),
                articleVO.isHasFile());
    }

}
