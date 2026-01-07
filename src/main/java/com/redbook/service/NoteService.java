package com.redbook.service;

import com.redbook.dto.*;
import com.redbook.entity.Note;
import com.redbook.entity.User;
import com.redbook.repository.NoteRepository;
import com.redbook.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ApiResponse<CreateNoteResponse> createNote(Long userId, String title, String content, String[] images) {
        Note note = new Note();
        note.setUserId(userId);
        note.setTitle(title);
        note.setContent(content);
        
        // 将图片数组转换为JSON字符串
        if (images != null && images.length > 0) {
            // 这里简单地将图片数组转为JSON字符串，实际应用中可能需要更复杂的处理
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < images.length; i++) {
                if (i > 0) sb.append(",");
                sb.append("\"").append(images[i]).append("\"");
            }
            sb.append("]");
            note.setImages(sb.toString());
        }
        
        note = noteRepository.save(note);

        CreateNoteResponse response = new CreateNoteResponse();
        response.setNoteId(note.getId());
        response.setTitle(note.getTitle());
        response.setImages(images);

        return ApiResponse.success(response);
    }

    public ApiResponse<FeedResponse> getFeed(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        
        // 获取用户关注的人发布的笔记
        // 这里简化实现，实际应用中需要从RelationService获取关注列表
        Page<Note> notePage = noteRepository.findAll(pageable);

        List<NoteItem> noteItems = new ArrayList<>();
        for (Note note : notePage.getContent()) {
            NoteItem item = new NoteItem();
            BeanUtils.copyProperties(note, item);
            item.setNoteId(note.getId());
            item.setUserId(note.getUserId());
            
            // 获取用户信息
            Optional<User> userOpt = userRepository.findById(note.getUserId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                item.setNickname(user.getNickname());
                item.setAvatarUrl(user.getAvatarUrl());
            }
            
            // 设置图片数组
            if (note.getImages() != null) {
                // 解析JSON字符串为数组
                String imagesStr = note.getImages();
                // 简单处理JSON字符串，实际应用中应使用JSON库
                imagesStr = imagesStr.replaceAll("[\\[\\]\"]", "");
                String[] images = imagesStr.split(",");
                item.setImages(images);
            }
            
            noteItems.add(item);
        }

        FeedResponse response = new FeedResponse();
        response.setList(noteItems);
        response.setHasMore(notePage.hasNext());

        return ApiResponse.success(response);
    }

    public ApiResponse<NoteDetailResponse> getNoteDetail(Long noteId) {
        Optional<Note> noteOpt = noteRepository.findById(noteId);
        if (!noteOpt.isPresent()) {
            return ApiResponse.error(404, "笔记不存在");
        }

        Note note = noteOpt.get();
        NoteItem item = new NoteItem();
        BeanUtils.copyProperties(note, item);
        item.setNoteId(note.getId());
        item.setUserId(note.getUserId());
        
        // 获取用户信息
        Optional<User> userOpt = userRepository.findById(note.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            item.setNickname(user.getNickname());
            item.setAvatarUrl(user.getAvatarUrl());
        }
        
        // 设置图片数组
        if (note.getImages() != null) {
            String imagesStr = note.getImages();
            imagesStr = imagesStr.replaceAll("[\\[\\]\"]", "");
            String[] images = imagesStr.split(",");
            item.setImages(images);
        }

        NoteDetailResponse response = new NoteDetailResponse();
        response.setData(item);

        return ApiResponse.success(response);
    }

    public ApiResponse<SearchResponse> searchNotes(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Note> notePage = noteRepository.findByTitleContainingOrContentContaining(keyword, pageable);

        List<SearchNoteItem> searchItems = new ArrayList<>();
        for (Note note : notePage.getContent()) {
            SearchNoteItem item = new SearchNoteItem();
            item.setNoteId(note.getId());
            item.setTitle(note.getTitle());
            
            // 设置封面图片
            if (note.getImages() != null && !note.getImages().isEmpty()) {
                // 简单取第一张图片作为封面
                String imagesStr = note.getImages();
                imagesStr = imagesStr.replaceAll("[\\[\\]\"]", "");
                String[] images = imagesStr.split(",");
                if (images.length > 0) {
                    item.setCoverImage(images[0]);
                }
            }
            
            item.setUserId(note.getUserId());
            
            // 获取用户信息
            Optional<User> userOpt = userRepository.findById(note.getUserId());
            if (userOpt.isPresent()) {
                item.setNickname(userOpt.get().getNickname());
            }
            
            searchItems.add(item);
        }

        SearchResponse response = new SearchResponse();
        response.setList(searchItems);

        return ApiResponse.success(response);
    }
}