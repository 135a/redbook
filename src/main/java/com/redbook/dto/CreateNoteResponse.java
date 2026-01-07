package com.redbook.dto;

import lombok.Data;

@Data
public class CreateNoteResponse {
    private Long noteId;
    private String title;
    private String[] images;
}