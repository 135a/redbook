package com.redbook.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class CreateCommentRequest {
    @NotBlank
    @Size(min = 1, max = 200, message = "评论内容长度必须在1-200个字符之间")
    private String content;
}