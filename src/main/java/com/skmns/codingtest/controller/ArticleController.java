package com.skmns.codingtest.controller;

import com.skmns.codingtest.dto.ArticleDTO;
import com.skmns.codingtest.service.ArticleService;
import com.skmns.codingtest.util.PaginationUtil;
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

    @GetMapping("/{id}")
    public ArticleDTO getArticle(@PathVariable Long id) {
        ArticleVO articleVO = articleService.getArticleWithFiles(id);
        return new ArticleDTO(
                articleVO.getArticleId(),
                articleVO.getTitle(),
                articleVO.getContent(),
                articleVO.getCreatedAt(),
                articleVO.getViewCount(),
                articleVO.isHasFile(),
                articleVO.getAuthorUsername());
    }

    @GetMapping
    public PaginationUtil<ArticleDTO> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationUtil<ArticleVO> articleVOs = articleService.getArticles(page, size);

        List<ArticleDTO> articleDTOs = articleVOs.getContent().stream()
                .map(vo -> new ArticleDTO(
                        vo.getArticleId(),
                        vo.getTitle(),
                        vo.getContent(),
                        vo.getCreatedAt(),
                        vo.getViewCount(),
                        vo.isHasFile(),
                        vo.getAuthorUsername()))
                .toList();

        return new PaginationUtil<>(articleDTOs, articleVOs.getTotalPages(),
                articleVOs.getCurrentPage(), articleVOs.getTotalElements());
    }

}
