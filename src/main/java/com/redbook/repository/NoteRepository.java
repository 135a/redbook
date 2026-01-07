package com.redbook.repository;

import com.redbook.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT n FROM Note n WHERE n.userId IN :followeeIds ORDER BY n.createdAt DESC")
    Page<Note> findByUserIdInOrderByCreatedAtDesc(@Param("followeeIds") List<Long> followeeIds, Pageable pageable);

    @Query("SELECT n FROM Note n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword%")
    Page<Note> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
}