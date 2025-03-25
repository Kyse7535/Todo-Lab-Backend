package com.example.todo.application;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.url}")
    private String redisUrl;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisStandaloneConfiguration());
    }

    private RedisStandaloneConfiguration redisStandaloneConfiguration() {
        URI redisUri = URI.create(redisUrl);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisUri.getHost());
        config.setPort(redisUri.getPort());
        if (redisUri.getUserInfo() != null) {
            String[] userInfo = redisUri.getUserInfo().split(":", 2);
            config.setPassword(userInfo[1]);
        }
        return config;
    }
}
