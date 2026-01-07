package com.redbook.dto;

import lombok.Data;

import java.util.List;

@Data
public class FeedResponse {
    private List<NoteItem> list;
    private Boolean hasMore;
}