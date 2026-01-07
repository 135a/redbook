package com.redbook.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private List<SearchNoteItem> list;
}