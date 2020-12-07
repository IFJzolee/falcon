package com.falcon.assessment.controller;

import static com.falcon.assessment.controller.PalindromeController.BASE_PATH;

import java.util.List;

import javax.validation.Valid;

import com.falcon.assessment.dto.CalculatedPalindromeDto;
import com.falcon.assessment.dto.PalindromeTaskDto;
import com.falcon.assessment.messaging.PalindromeTaskPublisher;
import com.falcon.assessment.service.PalindromeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping(BASE_PATH)
@Slf4j
@AllArgsConstructor
public class PalindromeController {

    // TODO maybe not on same paths?
    public static final String BASE_PATH = "/api/palindrome";

    private final PalindromeTaskPublisher palindromeTaskPublisher;
    private final PalindromeService palindromeService;

    @ApiOperation(value = "Create and broadcast a task")
    @PostMapping
    public void createPalindromeTask(@RequestBody @Valid PalindromeTaskDto request) {
        log.info("Received: {}", request);
        palindromeTaskPublisher.publish(request);
    }

    // TODO paging
    @ApiOperation(value = "Returns all the tasks with their longest palindromic substring length")
    @GetMapping
    public List<CalculatedPalindromeDto> getCalculatedPalindromes() {
        log.info("Get calculated palindromes");
        return palindromeService.getCalculatedPalindromes();
    }

}
