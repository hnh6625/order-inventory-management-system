package com.example.oims.integration;

import com.example.oims.shared.infrastructure.redis.RedisService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class RedisServiceIntegrationTest {
    @Autowired
    private RedisService redisService;

    private final String key = "test:redis:key";

    @AfterEach
    void tearDown() {
        redisService.delete(key);
    }

    @Test
    void shouldSetAndGetValue() {
        redisService.set(key, "hello", Duration.ofMinutes(1));
        String result = redisService.get(key);
        assertThat(result).isEqualTo("hello");
    }

    @Test
    void shouldExpireValueAfterTtl() throws InterruptedException {
        redisService.set(key, "hello", Duration.ofSeconds(2));

        assertThat(redisService.get(key)).isEqualTo("hello");

        Thread.sleep(2500);
        assertThat(redisService.get(key)).isNull();
    }

    @Test
    void sholudSetValueOnlyIfKeyDoesNotExist() {
        boolean first = redisService.setIfAbsent(
                key,
                "first",
                Duration.ofMinutes(1)
        );

        boolean second = redisService.setIfAbsent(
                key,
                "second",
                Duration.ofMinutes(1)
        );

        assertThat(first).isTrue();
        assertThat(second).isFalse();
        assertThat(redisService.get(key)).isEqualTo("first");
    }
}