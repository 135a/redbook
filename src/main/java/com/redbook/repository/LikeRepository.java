package com.redbook.repository;

import com.redbook.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndNoteId(Long userId, Long noteId);
    long countByNoteId(Long noteId);
    void deleteByUserIdAndNoteId(Long userId, Long noteId);
}