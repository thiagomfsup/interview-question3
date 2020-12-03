package com.example.demo.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.demo.vo.QuestionVO;

import lombok.Data;

@Data
@Entity
@Table(name = "TBL_QUESTIONS")
public class QuestionEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "message", nullable = false)
    private String message;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "questionId")
    private List<ReplyEntity> replies = new ArrayList<>();

    public QuestionVO toQuestionVO() {
        return new QuestionVO(id, author, message, replies.size());
    }
}
