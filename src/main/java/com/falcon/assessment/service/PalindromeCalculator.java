package com.falcon.assessment.service;

import static com.falcon.assessment.service.util.StringUtils.alphabeticSubstrings;

import java.util.List;

import com.falcon.assessment.dto.CalculatedPalindromeDto;
import org.springframework.stereotype.Component;

@Component
public class PalindromeCalculator {

    public CalculatedPalindromeDto calculate(PalindromeTask task) {
        // FIXME works with ascii only
        List<String> tokens = alphabeticSubstrings(task.getContent());
        int longestLength = 0;
        for (String token : tokens) {
            int length = longestSubPalindromeLength(token);
            if (length > longestLength) {
                longestLength = length;
            }
        }
        return new CalculatedPalindromeDto(task.getContent(), task.getTimestamp(), longestLength);
    }


    private int longestSubPalindromeLength(String s) {
        if (s == null || s.length() < 1) {
            return 0;
        }
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1).length();
    }

    private int expandAroundCenter(String s, int left, int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L--;
            R++;
        }
        return R - L - 1;
    }

}
