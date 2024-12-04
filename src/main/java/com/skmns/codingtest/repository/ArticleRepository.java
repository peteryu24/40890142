package com.skmns.codingtest.repository;

import com.skmns.codingtest.entity.ArticleEntity;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Page<ArticleEntity> findAll(Pageable pageable);

    Page<ArticleEntity> findByTitleContainingOrAuthorUsernameContaining(String title, String authorUsername,
            Pageable pageable);

    Optional<ArticleEntity> findById(Long id);
}
