package com.falcon.assessment.controller;

import static com.falcon.assessment.controller.LongestPalindromeController.BASE_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.falcon.assessment.config.ObjectMapperConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = LongestPalindromeController.class)
@Import(ObjectMapperConfiguration.class)
public class LongestPalindromeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PalindromeTaskPublisher palindromeTaskPublisher;

    @Test
    public void create_withNullValues() throws Exception {
        var request = new PalindromeTask(null, null);

        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content(json(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void create_withEmptyContent() throws Exception {
        var request = new PalindromeTask("", OffsetDateTime.now());

        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content(json(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void create_withInvalidDateFormat() throws Exception {
        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content("{\n"
                + "\"content\": \"abrakadabra\",\n"
                + "\"timestamp\": \"2018-10-09 00:12:12.359+0100\"\n"
                + "}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void create_properTimestampParsing() throws Exception {
        String content = "abrakadabra";
        OffsetDateTime timestamp = OffsetDateTime.of(2018, 10, 9, 0, 12, 12, 0, ZoneOffset.ofHours(1));
        PalindromeTask expectedTask = new PalindromeTask(content, timestamp);

        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content(String.format("{\n"
                + "\"content\": \"%s\",\n"
                + "\"timestamp\": \"2018-10-09 00:12:12+0100\"\n"
                + "}", content)))
            .andExpect(status().isOk());

        ArgumentCaptor<PalindromeTask> captor = ArgumentCaptor.forClass(PalindromeTask.class);
        verify(palindromeTaskPublisher).publish(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedTask);
    }

    private <T> String json(T obj) throws JsonProcessingException {
        String v = objectMapper.writeValueAsString(obj);
        return v;
    }

}
