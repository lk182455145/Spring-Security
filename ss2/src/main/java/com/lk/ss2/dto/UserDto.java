package com.lk.ss2.dto;

import com.lk.ss2.validation.annotation.ValidEmail;
import com.lk.ss2.validation.annotation.ValidPasswordMatches;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户
 */
@Data
@ValidPasswordMatches
public class UserDto implements Serializable {

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    /**
     * 用户名
     */
    @NotBlank
    @Size(min = 5, max = 10, message = "用户名必须5-10位")
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 确认密码
     */
    @NotBlank
    private String matchPassword;

    /**
     * 电子邮件
     */
    @NotBlank
    @ValidEmail
    private String email;

}
