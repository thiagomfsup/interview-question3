package com.example.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ReplyRepository replyRepository;

    public QuestionEntity postAQuestion(QuestionEntity question) {
        return questionRepository.save(question);
    }

    public ReplyEntity postAReply(Long questionId, ReplyEntity reply) {
        reply.setQuestion(retrieveAQuestion(questionId));

        return replyRepository.save(reply);
    }

    public QuestionEntity getAThread(Long questionId) {
        return retrieveAQuestion(questionId);
    }

    /**
     * Retrieve a question based on the <code>questionId<code> parameter.
     * 
     * @param questionId The question identifier.
     * @throws RecordNotFoundException if a question does not exist in the database.
     * @return
     */
    private QuestionEntity retrieveAQuestion(Long questionId) {
        Optional<QuestionEntity> question = questionRepository.findById(questionId);

        if (!question.isPresent())
            throw new RecordNotFoundException("No question record exist for given id");

        return question.get();
    }

    public List<QuestionEntity> listOfQuestions() {
        return questionRepository.findAll();
    }

}
