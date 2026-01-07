package com.redbook.dto;

import lombok.Data;

@Data
public class CollectResponse {
    private Boolean collected;
    private Integer collectCount;
}