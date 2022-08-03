package com.example.httpclient.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateDto {

    private Long id;
    private String status;
}
