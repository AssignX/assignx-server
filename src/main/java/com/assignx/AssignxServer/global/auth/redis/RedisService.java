package com.assignx.AssignxServer.global.auth.redis;

import com.assignx.AssignxServer.global.auth.exception.AuthExceptionUtils;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final String REFRESH_TOKEN_PREFIX = "refreshToken: ";

    public void saveRefreshToken(String key, String value) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + key, value, 7, TimeUnit.DAYS);
    }

    public String getRefreshToken(String key) {
        String existingRefreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + key);
        if (existingRefreshToken == null) {
            throw AuthExceptionUtils.RefreshTokenNotFound();
        }
        return existingRefreshToken;
    }

    public void deleteRefreshToken(String key) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + key);
    }
}