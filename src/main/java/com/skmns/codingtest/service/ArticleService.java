package com.skmns.codingtest.service;

import com.skmns.codingtest.entity.ArticleEntity;
import com.skmns.codingtest.entity.FileEntity;
import com.skmns.codingtest.entity.UserEntity;
import com.skmns.codingtest.repository.ArticleRepository;
import com.skmns.codingtest.repository.FileRepository;
import com.skmns.codingtest.util.PaginationUtil;
import com.skmns.codingtest.vo.ArticleVO;
import com.skmns.codingtest.vo.FileVO;

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

        /**
         * 게시글 상세 조회 (파일은 FileService에서 별도로 조회)
         */
        public ArticleVO getArticleDetails(Long articleId) {
                ArticleEntity article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

                return new ArticleVO(
                                article.getArticleId(),
                                article.getTitle(),
                                article.getContent(),
                                article.getCreatedAt(),
                                article.getViewCount(),
                                article.getAuthor().getUsername(),
                                article.isHasFile() // 파일 유무만 반환
                );
        }

        /**
         * 게시글 목록 조회 (페이징)
         * 기본 정보와 파일 유무만 포함
         */
        public PaginationUtil<ArticleVO> getPaginatedArticles(int page, int size) {
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
                                                article.isHasFile())) // 파일 유무만 포함
                                .toList();

                return new PaginationUtil<>(articles, articlePage.getTotalPages(), articlePage.getNumber(),
                                articlePage.getTotalElements());
        }

        /**
         * 게시글 생성 (파일은 FileService에서 처리)
         */
        @Transactional
        public void createArticle(ArticleVO articleVO, UserEntity user, List<MultipartFile> files)
                        throws IOException {
                // 게시글 생성
                ArticleEntity article = new ArticleEntity(
                                null,
                                articleVO.getTitle(),
                                articleVO.getContent(),
                                null, // createdAt은 @CreationTimestamp에 의해 자동 설정
                                0,
                                user);

                // 게시글 저장
                articleRepository.save(article);

                // 파일 처리 (파일 Service에 위임)
                if (files != null && !files.isEmpty()) {
                        fileService.attachFilesToArticle(files, article);
                }
        }

        @Transactional
        public void updateArticle(ArticleVO articleVO, UserEntity user, List<MultipartFile> files,
                        List<Long> deleteFileIds) throws IOException {
                ArticleEntity article = articleRepository.findById(articleVO.getArticleId())
                                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

                if (!article.getAuthor().getUserId().equals(user.getUserId())) {
                        throw new SecurityException("작성자만 게시글을 수정할 수 있습니다.");
                }

                article.setTitle(articleVO.getTitle());
                article.setContent(articleVO.getContent());

                // 삭제 요청된 파일 처리
                if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
                        fileService.deleteFilesByIds(deleteFileIds);
                }

                // 새 파일 처리
                if (files != null && !files.isEmpty()) {
                        fileService.attachFilesToArticle(files, article);
                }

                articleRepository.save(article);
        }

        @Transactional
        public void deleteArticle(Long articleId, UserEntity user) {
                // 게시글 조회 및 작성자 확인
                ArticleEntity article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

                if (!article.getAuthor().getUserId().equals(user.getUserId())) {
                        throw new SecurityException("작성자만 게시글을 삭제할 수 있습니다.");
                }

                // 파일 삭제
                fileService.deleteFilesByArticle(article);

                // 게시글 삭제
                articleRepository.delete(article);
        }

}
