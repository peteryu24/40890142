package com.skmns.codingtest.controller;

import com.skmns.codingtest.dto.ArticleDTO;
import com.skmns.codingtest.dto.FileDTO;
import com.skmns.codingtest.entity.AuthEntity;
import com.skmns.codingtest.service.ArticleService;
import com.skmns.codingtest.util.ArticleConverterUtil;
import com.skmns.codingtest.util.PaginationUtil;
import com.skmns.codingtest.util.SkmnsResult;
import com.skmns.codingtest.vo.ArticleVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 관련 API를 처리하는 컨트롤러 클래스입니다.
 * 게시글 조회, 목록 조회, 생성, 수정, 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {

        private final ArticleService articleService;

        /**
         * ArticleController 생성자
         * 
         * @param articleService 게시글 관련 서비스 클래스
         */
        public ArticleController(ArticleService articleService) {
                this.articleService = articleService;
        }

        /**
         * 특정 게시글의 상세 정보를 조회하는 API
         * 
         * <p>
         * 게시글의 ID를 입력받아 해당 게시글의 상세 정보를 반환합니다.
         * </p>
         * 
         * @param id 조회할 게시글의 ID
         * @return 게시글의 상세 정보
         */
        @GetMapping("/{id}")
        public SkmnsResult<ArticleDTO> getArticleDetails(@PathVariable Long id) {

                ArticleVO articleVO = articleService.getArticleDetails(id);
                ArticleDTO articleDTO = ArticleConverterUtil.toDTO(articleVO);

                return new SkmnsResult<>("게시글 조회 성공", HttpStatus.OK.value(), articleDTO);
        }

        /**
         * 게시글 목록을 페이징 처리하여 조회하는 API
         * 
         * <p>
         * 페이지 번호와 페이지 크기를 받아, 해당 페이지의 게시글 목록을 반환합니다.
         * </p>
         * 
         * @param page 페이지 번호 (기본값: 0)
         * @param size 페이지당 조회할 게시글 수 (기본값: 10)
         * @return 페이징된 게시글 목록
         */
        @GetMapping
        public SkmnsResult<PaginationUtil<ArticleDTO>> getPaginatedArticles(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "desc") String sortOrder,
                        @RequestParam(required = false) String searchQuery) {

                PaginationUtil<ArticleVO> articleVOs = articleService.getPaginatedArticles(page, size, sortOrder,
                                searchQuery);

                List<ArticleDTO> articleDTOs = articleVOs.getContent().stream()
                                .map(ArticleConverterUtil::toDTO)
                                .toList();

                PaginationUtil<ArticleDTO> articlePaginationDTO = new PaginationUtil<>(
                                articleDTOs,
                                articleVOs.getTotalPages(),
                                articleVOs.getCurrentPage(),
                                articleVOs.getTotalElements());

                return new SkmnsResult<>("게시글 목록 조회 성공", HttpStatus.OK.value(), articlePaginationDTO);
        }

        /**
         * 새로운 게시글을 작성하는 API
         * 
         * <p>
         * 사용자가 제목, 내용 및 선택적으로 파일을 포함하여 새로운 게시글을 작성할 수 있도록 합니다.
         * </p>
         * 
         * @param articleDTO 게시글 작성에 필요한 정보가 담긴 DTO 객체
         * @param fileDTO    파일 관련 정보
         * @param user       게시글 작성자 (로그인한 사용자)
         * @return 게시글 작성 성공 메시지
         * @throws IOException 파일 업로드 중 오류 발생 시 예외
         */
        @PostMapping(consumes = { "multipart/form-data" })
        public SkmnsResult<Void> createArticle(
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestPart(value = "files", required = false) List<MultipartFile> files,
                        @AuthenticationPrincipal AuthEntity user) throws IOException {

                ArticleVO articleVO = new ArticleVO(
                                null,
                                title,
                                content,
                                null, // createdAt은 null로 설정
                                0, // 초기 조회수는 0
                                user.getUsername(),
                                files != null && !files.isEmpty() // 첨부파일 여부
                );

                articleService.createArticle(articleVO, user, files);

                return new SkmnsResult<>("게시물이 작성되었습니다.", HttpStatus.CREATED.value());
        }

        /**
         * 게시글을 수정하는 API
         * 
         * <p>
         * 게시글의 제목과 내용을 수정하고, 선택적으로 파일을 추가하거나 삭제할 수 있습니다.
         * </p>
         * 
         * @param id         수정할 게시글의 ID
         * @param articleDTO 게시글 수정에 필요한 정보가 담긴 DTO 객체
         * @param fileDTO    파일 관련 정보
         * @param user       게시글 수정자 (로그인한 사용자)
         * @return 게시글 수정 성공 메시지
         * @throws IOException 파일 처리 중 오류 발생 시 예외
         */
        @PutMapping("/{id}")
        public SkmnsResult<Void> updateArticle(
                        @PathVariable Long id,
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestPart(value = "files", required = false) List<MultipartFile> files,
                        @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds, // Add
                                                                                                           // deleteFileIds
                        @AuthenticationPrincipal AuthEntity user) throws IOException {

                ArticleVO articleVO = new ArticleVO(id, title, content, null, 0, user.getUsername(),
                                files != null && !files.isEmpty());

                if (deleteFileIds == null) {
                        deleteFileIds = new ArrayList<>();
                }

                articleService.updateArticle(articleVO, user, files, deleteFileIds);

                return new SkmnsResult<>("게시물이 수정되었습니다.", HttpStatus.OK.value());
        }

        /**
         * 게시글을 삭제하는 API
         * 
         * <p>
         * 게시글의 ID를 입력받아 해당 게시글을 삭제합니다. 삭제자는 로그인한 사용자이어야 합니다.
         * </p>
         * 
         * @param id   삭제할 게시글의 ID
         * @param user 게시글 삭제자 (로그인한 사용자)
         * @return 게시글 삭제 성공 메시지
         */
        @DeleteMapping("/{id}")
        public SkmnsResult<Void> deleteArticle(
                        @PathVariable Long id,
                        @AuthenticationPrincipal AuthEntity user) {

                articleService.deleteArticle(id, user);
                return new SkmnsResult<>("게시물이 삭제되었습니다.", HttpStatus.OK.value());
        }
}
