package com.redbook.controller;

import com.redbook.annotation.CurrentUser;
import com.redbook.dto.*;
import com.redbook.service.InteractionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InteractionController {
    
    @Autowired
    private InteractionService interactionService;

    @PostMapping("/notes/{noteId}/like")
    public ResponseEntity<ApiResponse<LikeResponse>> likeNote(@CurrentUser Long userId, @PathVariable Long noteId) {
        ApiResponse<LikeResponse> response = interactionService.likeNote(userId, noteId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notes/{noteId}/collect")
    public ResponseEntity<ApiResponse<CollectResponse>> collectNote(@CurrentUser Long userId, @PathVariable Long noteId) {
        ApiResponse<CollectResponse> response = interactionService.collectNote(userId, noteId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notes/{noteId}/comments")
    public ResponseEntity<ApiResponse<CreateCommentResponse>> createComment(
            @CurrentUser Long userId,
            @PathVariable Long noteId,
            @Valid @RequestBody CreateCommentRequest request) {
        ApiResponse<CreateCommentResponse> response = interactionService.createComment(userId, noteId, request.getContent());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/notes/{noteId}/comments")
    public ResponseEntity<ApiResponse<List<CommentItem>>> getComments(
            @PathVariable Long noteId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiResponse<List<CommentItem>> response = interactionService.getComments(noteId, page, size);
        return ResponseEntity.ok(response);
    }
}