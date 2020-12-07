package com.falcon.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;

import com.falcon.assessment.dao.PalindromeTaskEntity;
import com.falcon.assessment.dto.CalculatedPalindrome;
import org.junit.Test;

public class PalindromeCalculatorTest {

    private PalindromeCalculator calculator = new PalindromeCalculator();

    @Test
    public void longestPalindromeCalculation() {
        assertThat(calculator.calculate(taskWithContent("")).getLongestPalindromeSize()).isEqualTo(0);
        assertThat(calculator.calculate(taskWithContent("a")).getLongestPalindromeSize()).isEqualTo(1);
        assertThat(calculator.calculate(taskWithContent("aba")).getLongestPalindromeSize()).isEqualTo(3);
        assertThat(calculator.calculate(taskWithContent("babad")).getLongestPalindromeSize()).isEqualTo(3);
    }

    @Test
    public void properFieldMapping() {
        var task = PalindromeTaskEntity.builder()
            .content("aba")
            .timestamp(OffsetDateTime.now())
            .build();

        CalculatedPalindrome calculatedPalindrome = calculator.calculate(task);

        assertThat(calculatedPalindrome.getContent()).isEqualTo(task.getContent());
        assertThat(calculatedPalindrome.getTimestamp()).isEqualTo(task.getTimestamp());
        assertThat(calculatedPalindrome.getLongestPalindromeSize()).isEqualTo(3);
    }

    private PalindromeTaskEntity taskWithContent(String content) {
        return PalindromeTaskEntity.builder()
            .content(content)
            .build();
    }

}
