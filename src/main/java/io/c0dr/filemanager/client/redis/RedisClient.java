package io.c0dr.filemanager.client.redis;

import io.c0dr.filemanager.service.model.UrlFileModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class RedisClient {

    private final RedisTemplate<String, UrlFileModel> redisTemplate;

    public void addUrl(String key, UrlFileModel value, long minutesToLive) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, Duration.of(minutesToLive, ChronoUnit.MINUTES));
    }

    public UrlFileModel getLocationFromUrlSuffix(String urlSuffix) {
        return redisTemplate.opsForValue().get(urlSuffix);
    }
}
