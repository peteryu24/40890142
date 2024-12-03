package com.skmns.codingtest.controller;

import com.skmns.codingtest.dto.ArticleDTO;
import com.skmns.codingtest.entity.UserEntity;
import com.skmns.codingtest.service.ArticleService;
import com.skmns.codingtest.util.PaginationUtil;
import com.skmns.codingtest.vo.ArticleVO;

import org.springframework.http.ResponseEntity;
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
        public ArticleDTO getArticleDetails(@PathVariable Long id) {
                ArticleVO articleVO = articleService.getArticleDetails(id);
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
        public PaginationUtil<ArticleDTO> getPaginatedArticles(@RequestParam(defaultValue = "0") int page,
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

                return new PaginationUtil<>(articleDTOs, articleVOs.getTotalPages(),
                                articleVOs.getCurrentPage(), articleVOs.getTotalElements());
        }

        @PostMapping(consumes = { "multipart/form-data" })
        public ResponseEntity<String> createArticle(
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam(value = "files", required = false) List<MultipartFile> files,
                        @AuthenticationPrincipal UserEntity user) throws IOException {

                ArticleVO articleVO = new ArticleVO(null, title, content, null, 0, user.getUsername(),
                                files != null && !files.isEmpty());
                articleService.createArticle(articleVO, user, files);

                return ResponseEntity.ok("게시물이 작성되었습니다.");
        }

        @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
        public ResponseEntity<String> updateArticle(
                        @PathVariable Long id,
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam(value = "files", required = false) List<MultipartFile> files,
                        @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds,
                        @AuthenticationPrincipal UserEntity user) throws IOException {

                ArticleVO articleVO = new ArticleVO(id, title, content, null, 0, user.getUsername(),
                                files != null && !files.isEmpty());
                articleService.updateArticle(articleVO, user, files, deleteFileIds);

                return ResponseEntity.ok("게시물이 수정되었습니다.");
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteArticle(
                        @PathVariable Long id,
                        @AuthenticationPrincipal UserEntity user) {

                articleService.deleteArticle(id, user);
                return ResponseEntity.ok("게시물이 삭제되었습니다.");
        }

}
