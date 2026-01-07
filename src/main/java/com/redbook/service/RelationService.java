package com.redbook.service;

import com.redbook.dto.FollowResponse;
import com.redbook.dto.ApiResponse;
import com.redbook.entity.Relation;
import com.redbook.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RelationService {
    
    @Autowired
    private RelationRepository relationRepository;

    @Transactional
    public ApiResponse<FollowResponse> follow(Long followerId, Long followeeId) {
        // 检查是否已经关注
        Optional<Relation> existingRelation = relationRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
        
        if (existingRelation.isPresent()) {
            // 取消关注
            relationRepository.delete(existingRelation.get());
            FollowResponse response = new FollowResponse();
            response.setFollowing(false);
            return ApiResponse.success(response);
        } else {
            // 关注
            Relation relation = new Relation();
            relation.setFollowerId(followerId);
            relation.setFolloweeId(followeeId);
            relationRepository.save(relation);
            
            FollowResponse response = new FollowResponse();
            response.setFollowing(true);
            return ApiResponse.success(response);
        }
    }

    public int getFollowingCount(Long userId) {
        return Math.toIntExact(relationRepository.countByFollowerId(userId));
    }

    public int getFollowerCount(Long userId) {
        return Math.toIntExact(relationRepository.countByFolloweeId(userId));
    }
}