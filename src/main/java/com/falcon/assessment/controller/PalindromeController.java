package com.falcon.assessment.controller;

import static com.falcon.assessment.controller.PalindromeController.BASE_PATH;

import java.util.List;

import javax.validation.Valid;

import com.falcon.assessment.dto.CalculatedPalindromeDto;
import com.falcon.assessment.dto.PalindromeTaskDto;
import com.falcon.assessment.messaging.PalindromeTaskPublisher;
import com.falcon.assessment.service.PalindromeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BASE_PATH)
@Slf4j
@AllArgsConstructor
public class PalindromeController {

    // TODO not on same paths?
    public static final String BASE_PATH = "/api/palindrome";

    private final PalindromeTaskPublisher palindromeTaskPublisher;
    private final PalindromeService palindromeService;

    @PostMapping
    public void createPalindromeTask(@RequestBody @Valid PalindromeTaskDto request) {
        log.info("Received: {}", request);
        palindromeTaskPublisher.publish(request);
    }

    // TODO paging
    @GetMapping
    public List<CalculatedPalindromeDto> getCalculatedPalindromes() {
        log.info("Get calculated palindromes");
        return palindromeService.getCalculatedPalindromes();
    }

}
