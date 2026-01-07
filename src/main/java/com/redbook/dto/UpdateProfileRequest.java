package com.redbook.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateProfileRequest {
    @Length(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Length(max = 100, message = "简介长度不能超过100个字符")
    private String bio;
}