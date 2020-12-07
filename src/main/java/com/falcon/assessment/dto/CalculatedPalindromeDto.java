package com.falcon.assessment.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CalculatedPalindromeDto {

    @JsonProperty("longest_palindrome_size")
    private final int longestPalindromeSize;
    private final String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private final OffsetDateTime timestamp;

    @JsonCreator
    public CalculatedPalindromeDto(
        @JsonProperty("content") String content,
        @JsonProperty("timestamp") OffsetDateTime timestamp,
        @JsonProperty("longest_palindrome_size") int longestPalindromeSize) {
        this.content = content;
        this.timestamp = timestamp;
        this.longestPalindromeSize = longestPalindromeSize;
    }

}
