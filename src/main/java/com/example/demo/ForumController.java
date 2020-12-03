package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.persistence.ForumService;
import com.example.demo.persistence.QuestionEntity;
import com.example.demo.persistence.ReplyEntity;
import com.example.demo.vo.QuestionVO;
import com.example.demo.vo.ReplyVO;

@RestController
public class ForumController {

    @Autowired
    private ForumService forumService;

    @PostMapping("/questions")
    public ResponseEntity<QuestionVO> postAQuestion(@RequestBody QuestionEntity question) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.postAQuestion(question).toQuestionVO());
    }

    @PostMapping("/questions/{id}/reply")
    public ResponseEntity<ReplyVO> postAReply(@PathVariable Long id, @RequestBody ReplyEntity reply) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.postAReply(id, reply).toReplyVO());
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionEntity> getAThread(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.getAThread(id));
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionEntity>> listOfQuestions() {
        return ResponseEntity.ok(forumService.listOfQuestions());
    }
}
