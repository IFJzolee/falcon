package com.falcon.assessment.controller;

import static com.falcon.assessment.controller.LongestPalindromeController.BASE_PATH;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BASE_PATH)
@Slf4j
public class LongestPalindromeController {

    public static final String BASE_PATH = "/palindrome";

    private final PalindromeTaskPublisher palindromeTaskPublisher;

    public LongestPalindromeController(PalindromeTaskPublisher palindromeTaskPublisher) {
        this.palindromeTaskPublisher = palindromeTaskPublisher;
    }

    @PostMapping
    public void createPalindrome(@RequestBody @Valid PalindromeTask request) {
        log.info("Received: {}", request);
        palindromeTaskPublisher.publish(request);
    }

}
