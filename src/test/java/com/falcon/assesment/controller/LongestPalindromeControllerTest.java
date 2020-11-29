package com.falcon.assesment.controller;

import static com.falcon.assesment.controller.LongestPalindromeController.BASE_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;

import com.falcon.assesment.config.ObjectMapperConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

    @Test
    public void create_withNullValues() throws Exception {
        var request = new CreatePalindromeRequest(null, null);

        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content(json(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void create_withEmptyContent() throws Exception {
        var request = new CreatePalindromeRequest("", OffsetDateTime.now());

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
        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content("{\n"
                + "\"content\": \"abrakadabra\",\n"
                + "\"timestamp\": \"2018-10-09 00:12:12+0100\"\n"
                + "}"))
            .andExpect(status().isOk());

        // TODO verify once we have a service mock
    }

    private <T> String json(T obj) throws JsonProcessingException {
        String v = objectMapper.writeValueAsString(obj);
        return v;
    }

}
