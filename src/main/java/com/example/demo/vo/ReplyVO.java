package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyVO {

    private Long id;

    private Long questionId;

    private String author;

    private String message;
}
