package com.falcon.assessment.listener;

import com.falcon.assessment.controller.PalindromeTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PalindromeTaskListener implements MessageListener {

    private final Jackson2JsonRedisSerializer<PalindromeTask> serializer;

    public PalindromeTaskListener(Jackson2JsonRedisSerializer<PalindromeTask> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PalindromeTask palindromeTask = serializer.deserialize(message.getBody());
        log.info("Received from topic `{}`, message: {}", new String(pattern), palindromeTask);

        handleTask(palindromeTask);
    }

    private void handleTask(PalindromeTask task) {
    }

}
