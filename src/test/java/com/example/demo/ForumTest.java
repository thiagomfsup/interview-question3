package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.persistence.QuestionEntity;
import com.example.demo.persistence.QuestionRepository;
import com.example.demo.persistence.ReplyEntity;
import com.example.demo.persistence.ReplyRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ForumTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private ReplyRepository replyRepository;

    /**
     * When an invalid ID is received when getting a thread then the service should
     * return HTTP 404.
     */
    @Test
    public void given_anInvalidId_when_gettingAThread_then_returnNotFoundStatus() throws Exception {
        Mockito.when(questionRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/questions/12345")).andExpect(status().isNotFound());
    }

    /**
     * When posting a Reply using an invalid Question ID, then the service should
     * return HTTP 404.
     */
    @Test
    public void given_anInvalidQuestionID_when_postingAReply_then_returnNotFoundStatus() throws Exception {
        this.mockMvc.perform(post("/questions/12345/reply")
                .content("{\"author\": \"Reply Autor\",\"message\": \"Message reply text\"}")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    /**
     * When posting a Question then save and return it. It should contains the
     * expected JSON fields and values.
     */
    @Test
    public void given_aValidQuestionInJSONFormat_when_postingAQuestion_then_saveAndReturn() throws Exception {
        Mockito.when(questionRepository.save(Mockito.any())).thenAnswer((invocation) -> {
            QuestionEntity question = (QuestionEntity) invocation.getArgument(0);
            question.setId(1L);
            return question;
        });

        this.mockMvc
                .perform(post("/questions").content("{\"author\": \"Daniel\",\"message\": \"Message text\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.author").value("Daniel")).andExpect(jsonPath("$.message").value("Message text"))
                .andExpect(jsonPath("$.replies").value(0L));
    }

    /**
     * When getting a Question (a Thread) using a valid Question ID, then return it.
     */
    @Test
    public void given_aValidQuestionId_when_gettingAThread_then_returnIt() throws Exception {
        QuestionEntity question = new QuestionEntity();
        question.setId(12345L);
        question.setAuthor("Test author");
        question.setMessage("Mock Message Text");

        Mockito.when(questionRepository.findById(12345L)).thenReturn(Optional.of(question));

        this.mockMvc.perform(get("/questions/12345")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("12345")).andExpect(jsonPath("$.author").value("Test author"))
                .andExpect(jsonPath("$.message").value("Mock Message Text"));
    }

    /**
     * When posting a Reply using a Valid Question ID then save and return it.
     */
    @Test
    public void given_AValidQuestionId_when_postingAReply_then_saveAndReturn() throws Exception {
        QuestionEntity question = new QuestionEntity();
        question.setId(12345L);

        Mockito.when(questionRepository.findById(12345L)).thenReturn(Optional.of(question));
        Mockito.when(replyRepository.save(Mockito.any())).thenAnswer((invocation) -> {
            ReplyEntity reply = (ReplyEntity) invocation.getArgument(0);
            reply.setId(1L);
            reply.setQuestion(question);
            return reply;
        });

        this.mockMvc
                .perform(post("/questions/12345/reply")
                        .content("{\"author\": \"Reply Autor\",\"message\": \"Message reply text\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.questionId").value(12345L)).andExpect(jsonPath("$.author").value("Reply Autor"))
                .andExpect(jsonPath("$.message").value("Message reply text"));
    }

    /**
     * When posting a Reply using an invalid question ID then the service should
     * return HTTP 404.
     */
    @Test
    public void given_AnInvalidQuestionId_when_postingAReply_then_returnNotFoundStatus() throws Exception {
        Mockito.when(questionRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/questions/12345/reply")
                .content("{\"author\": \"Reply Autor\",\"message\": \"Message reply text\"}")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    /**
     * Getting the list of Question when the database is empty (i.e. there is no
     * question posted) then an empty JSON array should be returned.
     */
    @Test
    public void given_emptyDatabase_when_gettingTheListOfQuestion_then_returnNoData() throws Exception {
        Mockito.when(questionRepository.findAll()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/questions")).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }
}
