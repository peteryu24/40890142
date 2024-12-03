package com.skmns.codingtest.repository;

import com.skmns.codingtest.entity.ArticleEntity;
import com.skmns.codingtest.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByArticle_ArticleId(Long articleId);

    void deleteAllByArticle(ArticleEntity article);
}
