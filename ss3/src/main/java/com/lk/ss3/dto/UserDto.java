package com.lk.ss3.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    private String name;

    private String email;
}
