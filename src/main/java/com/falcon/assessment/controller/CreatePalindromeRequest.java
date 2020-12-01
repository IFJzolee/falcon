package com.falcon.assessment.controller;

import java.time.OffsetDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreatePalindromeRequest {

    @Length(min = 1, max = 1000)
    private final String content;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private final OffsetDateTime timestamp;

    @JsonCreator
    public CreatePalindromeRequest(
        @JsonProperty("content") String content,
        @JsonProperty("timestamp") OffsetDateTime timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

}
