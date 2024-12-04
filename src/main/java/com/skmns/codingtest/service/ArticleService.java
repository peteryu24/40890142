package com.skmns.codingtest.service;

import com.skmns.codingtest.entity.ArticleEntity;
import com.skmns.codingtest.entity.AuthEntity;
import com.skmns.codingtest.entity.FileEntity;
import com.skmns.codingtest.repository.ArticleRepository;
import com.skmns.codingtest.repository.FileRepository;
import com.skmns.codingtest.util.PaginationUtil;
import com.skmns.codingtest.util.ArticleConverterUtil;
import com.skmns.codingtest.vo.ArticleVO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

        private final ArticleRepository articleRepository;
        private final FileRepository fileRepository;
        private final FileService fileService;

        public ArticleService(ArticleRepository articleRepository, FileService fileService,
                        FileRepository fileRepository) {
                this.articleRepository = articleRepository;
                this.fileService = fileService;
                this.fileRepository = fileRepository;
        }

        public ArticleVO getArticleDetails(Long articleId) {
                // 게시글을 데이터베이스에서 가져옴
                ArticleEntity article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

                // 파일 엔티티 목록을 가져와서 파일 이름만 추출
                List<FileEntity> fileEntities = fileRepository.findByArticle_ArticleId(articleId);
                List<String> fileNames = fileEntities.stream()
                                .map(FileEntity::getFileName) // FileEntity에서 fileName만 추출
                                .collect(Collectors.toList());

                // ArticleEntity를 ArticleVO로 변환
                ArticleVO articleVO = ArticleConverterUtil.toVO(article);

                // 파일 이름을 ArticleVO에 설정
                articleVO.setFileNames(fileNames);

                return articleVO;
        }

        public PaginationUtil<ArticleVO> getPaginatedArticles(int page, int size, String sortOrder,
                        String searchQuery) {
                Sort sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");

                Pageable pageable = PageRequest.of(page, size, sort);

                Page<ArticleEntity> articlePage;
                if (searchQuery != null && !searchQuery.isBlank()) {
                        articlePage = articleRepository.findByTitleContainingOrAuthorUsernameContaining(searchQuery,
                                        searchQuery, pageable);
                } else {
                        articlePage = articleRepository.findAll(pageable);
                }

                List<ArticleVO> articleVOs = articlePage.stream()
                                .map(ArticleConverterUtil::toVO)
                                .collect(Collectors.toList());

                return new PaginationUtil<>(articleVOs, articlePage.getTotalPages(), page,
                                articlePage.getTotalElements());
        }

        @Transactional
        public void createArticle(ArticleVO articleVO, AuthEntity user, List<MultipartFile> files) throws IOException {
                ArticleEntity article = new ArticleEntity(
                                null,
                                articleVO.getTitle(),
                                articleVO.getContent(),
                                null,
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

        public void increaseViewCount(Long articleId) {
                ArticleEntity article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
                article.setViewCount(article.getViewCount() + 1); // 조회수 증가
                articleRepository.save(article);
        }
}
