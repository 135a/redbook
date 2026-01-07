package com.redbook.service;

import com.redbook.dto.*;
import com.redbook.entity.*;
import com.redbook.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InteractionService {
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private CollectionRepository collectionRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ApiResponse<LikeResponse> likeNote(Long userId, Long noteId) {
        Optional<Note> noteOpt = noteRepository.findById(noteId);
        if (!noteOpt.isPresent()) {
            return ApiResponse.error(2001, "笔记不存在");
        }

        Note note = noteOpt.get();
        Optional<Like> existingLikeOpt = likeRepository.findByUserIdAndNoteId(userId, noteId);

        boolean liked;
        if (existingLikeOpt.isPresent()) {
            // 取消点赞
            likeRepository.delete(existingLikeOpt.get());
            note.setLikeCount(Math.max(0, note.getLikeCount() - 1));
            liked = false;
        } else {
            // 点赞
            Like like = new Like();
            like.setUserId(userId);
            like.setNoteId(noteId);
            likeRepository.save(like);
            note.setLikeCount(note.getLikeCount() + 1);
            liked = true;
        }

        noteRepository.save(note);

        LikeResponse response = new LikeResponse();
        response.setLiked(liked);
        response.setLikeCount(note.getLikeCount());

        return ApiResponse.success(response);
    }

    @Transactional
    public ApiResponse<CollectResponse> collectNote(Long userId, Long noteId) {
        Optional<Note> noteOpt = noteRepository.findById(noteId);
        if (!noteOpt.isPresent()) {
            return ApiResponse.error(2001, "笔记不存在");
        }

        Note note = noteOpt.get();
        Optional<Collection> existingCollectionOpt = collectionRepository.findByUserIdAndNoteId(userId, noteId);

        boolean collected;
        if (existingCollectionOpt.isPresent()) {
            // 取消收藏
            collectionRepository.delete(existingCollectionOpt.get());
            note.setCollectCount(Math.max(0, note.getCollectCount() - 1));
            collected = false;
        } else {
            // 收藏
            Collection collection = new Collection();
            collection.setUserId(userId);
            collection.setNoteId(noteId);
            collectionRepository.save(collection);
            note.setCollectCount(note.getCollectCount() + 1);
            collected = true;
        }

        noteRepository.save(note);

        CollectResponse response = new CollectResponse();
        response.setCollected(collected);
        response.setCollectCount(note.getCollectCount());

        return ApiResponse.success(response);
    }

    @Transactional
    public ApiResponse<CreateCommentResponse> createComment(Long userId, Long noteId, String content) {
        Optional<Note> noteOpt = noteRepository.findById(noteId);
        if (!noteOpt.isPresent()) {
            return ApiResponse.error(2001, "笔记不存在");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ApiResponse.error(404, "用户不存在");
        }

        Comment comment = new Comment();
        comment.setNoteId(noteId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment = commentRepository.save(comment);

        // 更新笔记的评论数
        Note note = noteOpt.get();
        note.setCommentCount(note.getCommentCount() + 1);
        noteRepository.save(note);

        CreateCommentResponse response = new CreateCommentResponse();
        response.setCommentId(comment.getId());
        response.setContent(comment.getContent());
        response.setUserId(comment.getUserId());
        response.setNickname(userOpt.get().getNickname());
        response.setCreatedAt(comment.getCreatedAt());

        return ApiResponse.success(response);
    }

    public ApiResponse<List<CommentItem>> getComments(Long noteId, int page, int size) {
        // 这里简化实现，实际应用中需要分页
        List<Comment> comments = commentRepository.findByNoteIdOrderByCreatedAtAsc(noteId, 
            org.springframework.data.domain.PageRequest.of(page - 1, size)).getContent();

        List<CommentItem> commentItems = new ArrayList<>();
        for (Comment comment : comments) {
            CommentItem item = new CommentItem();
            item.setCommentId(comment.getId());
            item.setContent(comment.getContent());
            item.setUserId(comment.getUserId());
            
            // 获取用户信息
            Optional<User> userOpt = userRepository.findById(comment.getUserId());
            if (userOpt.isPresent()) {
                item.setNickname(userOpt.get().getNickname());
            }
            item.setCreatedAt(comment.getCreatedAt());
            
            commentItems.add(item);
        }

        return ApiResponse.success(commentItems);
    }
}