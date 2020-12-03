package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionVO {

    private Long id;

    private String author;

    private String message;

    private int replies;
}
