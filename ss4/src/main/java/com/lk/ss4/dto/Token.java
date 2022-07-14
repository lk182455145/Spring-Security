package com.lk.ss4.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.io.Serializable;

@Data
@With
@Builder
public class Token implements Serializable {

    private String accessToken;

    private String refreshToken;
}
