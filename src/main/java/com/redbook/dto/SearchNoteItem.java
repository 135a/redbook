package com.redbook.dto;

import lombok.Data;

@Data
public class SearchNoteItem {
    private Long noteId;
    private String title;
    private String coverImage;
    private Long userId;
    private String nickname;
}