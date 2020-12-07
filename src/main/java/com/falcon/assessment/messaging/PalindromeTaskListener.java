package com.falcon.assessment.messaging;

import com.falcon.assessment.dto.PalindromeTask;
import com.falcon.assessment.service.PalindromeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class PalindromeTaskListener implements MessageListener {

    private final Jackson2JsonRedisSerializer<PalindromeTask> serializer;
    private final PalindromeService palindromeService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PalindromeTask task = serializer.deserialize(message.getBody());
        log.info("Received from topic `{}`, message: {}", new String(pattern), task);

        palindromeService.processTask(task);
    }

}
