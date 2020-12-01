package com.falcon.assessment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisStandaloneConfiguration redisConfig(
        @Value("${redis.host}") String host,
        @Value("${redis.port}") int port) {

        return new RedisStandaloneConfiguration(host, port);
    }

    // TODO pooling
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration jedisConfig) {
        return new JedisConnectionFactory(jedisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connFactory);
        return template;
    }

}
