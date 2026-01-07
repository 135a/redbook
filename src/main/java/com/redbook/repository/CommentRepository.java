package com.redbook.repository;

import com.redbook.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByNoteIdOrderByCreatedAtAsc(Long noteId, Pageable pageable);
    long countByNoteId(Long noteId);
}