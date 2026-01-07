package com.redbook.repository;

import com.redbook.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    Optional<Relation> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    
    @Query("SELECT r.followeeId FROM Relation r WHERE r.followerId = :followerId")
    List<Long> findFolloweeIdsByFollowerId(@Param("followerId") Long followerId);
    
    @Query("SELECT r.followerId FROM Relation r WHERE r.followeeId = :followeeId")
    List<Long> findFollowerIdsByFolloweeId(@Param("followeeId") Long followeeId);
    
    long countByFollowerId(Long userId);
    
    long countByFolloweeId(Long userId);
}