package com.skmns.codingtest.service;

import com.skmns.codingtest.entity.ArticleEntity;
import com.skmns.codingtest.entity.AuthEntity;
import com.skmns.codingtest.repository.ArticleRepository;
import com.skmns.codingtest.util.PaginationUtil;
import com.skmns.codingtest.util.ArticleConverterUtil;
import com.skmns.codingtest.vo.ArticleVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ArticleService {

        private final ArticleRepository articleRepository;
        private final FileService fileService;

        public ArticleService(ArticleRepository articleRepository, FileService fileService) {
                this.articleRepository = articleRepository;
                this.fileService = fileService;
        }

        public ArticleVO getArticleDetails(Long articleId) {
                ArticleEntity article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

                return ArticleConverterUtil.toVO(article);
        }

        public PaginationUtil<ArticleVO> getPaginatedArticles(int page, int size) {
                PageRequest pageRequest = PageRequest.of(page, size);
                Page<ArticleEntity> articlePage = articleRepository.findAll(pageRequest);

                List<ArticleVO> articles = articlePage.getContent().stream()
                                .map(ArticleConverterUtil::toVO)
                                .toList();

                return new PaginationUtil<>(articles, articlePage.getTotalPages(), articlePage.getNumber(),
                                articlePage.getTotalElements());
        }

        @Transactional
        public void createArticle(ArticleVO articleVO, AuthEntity user, List<MultipartFile> files) throws IOException {
                ArticleEntity article = new ArticleEntity(
                                null,
                                articleVO.getTitle(),
                                articleVO.getContent(),
                                null, // @CreationTimestamp
                                0,
                                user);

                articleRepository.save(article);

                if (files != null && !files.isEmpty()) {
                        fileService.attachFilesToArticle(files, article);
                }
        }

        @Transactional
        public void updateArticle(
                        ArticleVO articleVO,
                        AuthEntity user,
                        List<MultipartFile> newFiles,
                        List<Long> deleteFileIds) throws IOException {

                ArticleEntity article = articleRepository.findById(articleVO.getArticleId())
                                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

                if (!article.getAuthor().getUserId().equals(user.getUserId())) {
                        throw new SecurityException("작성자만 게시글을 수정할 수 있습니다.");
                }

                article.setTitle(articleVO.getTitle());
                article.setContent(articleVO.getContent());

                if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
                        fileService.deleteFilesByIds(deleteFileIds);
                }

                if (newFiles != null && !newFiles.isEmpty()) {
                        fileService.attachFilesToArticle(newFiles, article);
                }

                articleRepository.save(article);
        }

        @Transactional
        public void deleteArticle(Long articleId, AuthEntity user) {
                ArticleEntity article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

                if (!article.getAuthor().getUserId().equals(user.getUserId())) {
                        throw new SecurityException("작성자만 게시글을 삭제할 수 있습니다.");
                }

                fileService.deleteFilesByArticle(article);
                articleRepository.delete(article);
        }
}
