package com.falcon.assessment.controller;

import static com.falcon.assessment.controller.PalindromeController.BASE_PATH;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.falcon.assessment.config.ObjectMapperConfiguration;
import com.falcon.assessment.dto.CalculatedPalindromeDto;
import com.falcon.assessment.dto.PalindromeTaskDto;
import com.falcon.assessment.messaging.PalindromeTaskPublisher;
import com.falcon.assessment.service.PalindromeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
@WebMvcTest(controllers = PalindromeController.class)
@Import(ObjectMapperConfiguration.class)
public class PalindromeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PalindromeTaskPublisher palindromeTaskPublisherMock;
    @MockBean
    private PalindromeService palindromeServiceMock;

    @Test
    public void createTask_withNullValues() throws Exception {
        var request = new PalindromeTaskDto(null, null);

        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content(json(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createTask_withEmptyContent() throws Exception {
        var request = new PalindromeTaskDto("", OffsetDateTime.now());

        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content(json(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createTask_withInvalidDateFormat() throws Exception {
        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content("{\n"
                + "\"content\": \"abrakadabra\",\n"
                + "\"timestamp\": \"2018-10-09 00:12:12.359+0100\"\n"
                + "}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createTask_properTimestampParsing() throws Exception {
        String content = "abrakadabra";
        OffsetDateTime timestamp = OffsetDateTime.of(2018, 10, 9, 0, 12, 12, 0, ZoneOffset.ofHours(1));
        PalindromeTaskDto expectedTask = new PalindromeTaskDto(content, timestamp);

        mockMvc.perform(post(BASE_PATH)
            .contentType(APPLICATION_JSON)
            .content(String.format("{\n"
                + "\"content\": \"%s\",\n"
                + "\"timestamp\": \"2018-10-09 00:12:12+0100\"\n"
                + "}", content)))
            .andExpect(status().isOk());

        ArgumentCaptor<PalindromeTaskDto> captor = ArgumentCaptor.forClass(PalindromeTaskDto.class);
        verify(palindromeTaskPublisherMock).publish(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedTask);
    }

    @Test
    public void getCalculatedPalindromes_emptyList() throws Exception {
        when(palindromeServiceMock.getCalculatedPalindromes()).thenReturn(emptyList());

        mockMvc.perform(get(BASE_PATH)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void getCalculatedPalindromes() throws Exception {
        var calcdPalindrome1 = new CalculatedPalindromeDto("a", OffsetDateTime.parse("2007-12-03T10:15:30+02:00"), 1);
        var calcdPalindrome2 = new CalculatedPalindromeDto("aba", OffsetDateTime.parse("2007-12-03T05:15:30+03:00"), 3);
        var result = List.of(calcdPalindrome1, calcdPalindrome2);
        when(palindromeServiceMock.getCalculatedPalindromes()).thenReturn(result);

        String respBody = mockMvc.perform(get(BASE_PATH)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<CalculatedPalindromeDto> actualResult = objectMapper.readValue(respBody,
            new TypeReference<List<CalculatedPalindromeDto>>() {
            });
        assertThat(actualResult).isEqualTo(result);
    }

    private <T> String json(T obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

}
