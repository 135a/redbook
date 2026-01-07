package com.redbook.controller;

import com.redbook.annotation.CurrentUser;
import com.redbook.dto.*;
import com.redbook.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    
    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateNoteResponse>> createNote(
            @CurrentUser Long userId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "images", required = false) MultipartFile[] images) throws IOException {
        // 处理图片上传
        String[] imageUrls = null;
        if (images != null && images.length > 0) {
            imageUrls = new String[images.length];
            for (int i = 0; i < images.length; i++) {
                // 这里需要实现图片上传逻辑，暂时使用模拟URL
                imageUrls[i] = "https://example.com/images/" + System.currentTimeMillis() + "_" + images[i].getOriginalFilename();
            }
        }
        
        ApiResponse<CreateNoteResponse> response = noteService.createNote(userId, title, content, imageUrls);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<FeedResponse>> getFeed(
            @CurrentUser Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiResponse<FeedResponse> response = noteService.getFeed(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<ApiResponse<NoteDetailResponse>> getNoteDetail(@PathVariable Long noteId) {
        ApiResponse<NoteDetailResponse> response = noteService.getNoteDetail(noteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SearchResponse>> searchNotes(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiResponse<SearchResponse> response = noteService.searchNotes(keyword, page, size);
        return ResponseEntity.ok(response);
    }
}