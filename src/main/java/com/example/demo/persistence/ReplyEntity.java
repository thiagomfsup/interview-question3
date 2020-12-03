package com.example.demo.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.demo.vo.ReplyVO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "TBL_REPLIES")
public class ReplyEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId")
    @JsonIgnore
    private QuestionEntity question;

    public ReplyVO toReplyVO() {
        return new ReplyVO(id, question.getId(), author, message);
    }
}
