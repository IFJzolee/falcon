package com.falcon.assessment.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CalculatedPalindrome extends PalindromeTask {

    @JsonProperty("longest_palindrome_size")
    private final int longestPalindromeSize;

    @JsonCreator
    public CalculatedPalindrome(
        @JsonProperty("content") String content,
        @JsonProperty("timestamp") OffsetDateTime timestamp,
        @JsonProperty("longest_palindrome_size") int longestPalindromeSize) {
        super(content, timestamp);
        this.longestPalindromeSize = longestPalindromeSize;
    }

}
