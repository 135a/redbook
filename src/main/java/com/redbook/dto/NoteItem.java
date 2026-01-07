package com.redbook.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteItem {
    private Long noteId;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String title;
    private String content;
    private String[] images;
    private Integer likeCount;
    private Integer collectCount;
    private Integer commentCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}