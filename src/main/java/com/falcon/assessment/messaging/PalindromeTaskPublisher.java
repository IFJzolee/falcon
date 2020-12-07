package com.falcon.assessment.messaging;

import com.falcon.assessment.dto.PalindromeTaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PalindromeTaskPublisher {

    private final RedisTemplate<String, PalindromeTaskDto> redisTemplate;
    private final String topic;

    public PalindromeTaskPublisher(
        RedisTemplate<String, PalindromeTaskDto> redisTemplate,
        @Value("${palindrome-task.topic}") String topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(PalindromeTaskDto message) {
        log.info("Publishing to topic `{}`, message: {}", topic, message);
        redisTemplate.convertAndSend(topic, message);
    }

}
