package com.redbook.dto;

import lombok.Data;

@Data
public class LikeResponse {
    private Boolean liked;
    private Integer likeCount;
}