package com.falcon.assessment.config;

import com.falcon.assessment.controller.PalindromeTask;
import com.falcon.assessment.listener.PalindromeTaskListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class PubSubConfiguration {

    @Bean
    public Jackson2JsonRedisSerializer<PalindromeTask> palindromeTaskSerializer(ObjectMapper objectMapper) {
        var serializer = new Jackson2JsonRedisSerializer(PalindromeTask.class);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    public RedisTemplate<String, PalindromeTask> redisTemplate(JedisConnectionFactory connFactory,
        RedisSerializer<PalindromeTask> serializer) {
        RedisTemplate<String, PalindromeTask> template = new RedisTemplate<>();
        template.setConnectionFactory(connFactory);
        template.setValueSerializer(serializer);
        return template;
    }

    @Bean
    public MessageListenerAdapter palindromeTaskMessageListener(PalindromeTaskListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public ChannelTopic palindromeTopic(@Value("${palindrome-task.topic}") String topic) {
        return new ChannelTopic(topic);
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(
        JedisConnectionFactory connectionFactory,
        MessageListenerAdapter palindromeTaskMessageListener,
        ChannelTopic palindromeTopic
    ) {
        var container = new RedisMessageListenerContainer();
        container.addMessageListener(palindromeTaskMessageListener, palindromeTopic);
        container.setConnectionFactory(connectionFactory);
        return container;
    }

}
