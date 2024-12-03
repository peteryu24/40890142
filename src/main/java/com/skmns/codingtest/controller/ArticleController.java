package com.skmns.codingtest.controller;

import com.skmns.codingtest.dto.ArticleDTO;
import com.skmns.codingtest.entity.AuthEntity;
import com.skmns.codingtest.service.ArticleService;
import com.skmns.codingtest.util.PaginationUtil;
import com.skmns.codingtest.util.SkmnsResult;
import com.skmns.codingtest.vo.ArticleVO;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

        private final ArticleService articleService;

        public ArticleController(ArticleService articleService) {
                this.articleService = articleService;
        }

        @GetMapping("/{id}")
        public SkmnsResult<ArticleDTO> getArticleDetails(@PathVariable Long id) {
                ArticleVO articleVO = articleService.getArticleDetails(id);
                ArticleDTO articleDTO = new ArticleDTO(
                                articleVO.getArticleId(),
                                articleVO.getTitle(),
                                articleVO.getContent(),
                                articleVO.getCreatedAt(),
                                articleVO.getViewCount(),
                                articleVO.isHasFile(),
                                articleVO.getAuthorUsername());

                return new SkmnsResult<>("게시글 조회 성공", HttpStatus.OK.value(), articleDTO);
        }

        @GetMapping
        public SkmnsResult<PaginationUtil<ArticleDTO>> getPaginatedArticles(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                PaginationUtil<ArticleVO> articleVOs = articleService.getPaginatedArticles(page, size);
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

                PaginationUtil<ArticleDTO> articlePaginationDTO = new PaginationUtil<>(
                                articleDTOs,
                                articleVOs.getTotalPages(),
                                articleVOs.getCurrentPage(),
                                articleVOs.getTotalElements());

                return new SkmnsResult<>("게시글 목록 조회 성공", HttpStatus.OK.value(), articlePaginationDTO);
        }

        @PostMapping(consumes = { "multipart/form-data" })
        public SkmnsResult<Void> createArticle(
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam(value = "files", required = false) List<MultipartFile> files,
                        @AuthenticationPrincipal AuthEntity user) throws IOException {

                ArticleVO articleVO = new ArticleVO(null, title, content, null, 0, user.getUsername(),
                                files != null && !files.isEmpty());
                articleService.createArticle(articleVO, user, files);

                return new SkmnsResult<>("게시물이 작성되었습니다.", HttpStatus.CREATED.value());
        }

        @PutMapping("/{id}")
        public SkmnsResult<Void> updateArticle(
                        @PathVariable Long id,
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam(value = "files", required = false) List<MultipartFile> newFiles,
                        @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds,
                        @AuthenticationPrincipal AuthEntity user) throws IOException {

                ArticleVO articleVO = new ArticleVO(id, title, content, null, 0, user.getUsername(),
                                newFiles != null && !newFiles.isEmpty());

                articleService.updateArticle(articleVO, user, newFiles, deleteFileIds);

                return new SkmnsResult<>("게시물이 수정되었습니다.", HttpStatus.OK.value());
        }

        @DeleteMapping("/{id}")
        public SkmnsResult<Void> deleteArticle(
                        @PathVariable Long id,
                        @AuthenticationPrincipal AuthEntity user) {

                articleService.deleteArticle(id, user);

                return new SkmnsResult<>("게시물이 삭제되었습니다.", HttpStatus.OK.value());
        }
}
