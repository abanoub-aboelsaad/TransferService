package org.transferservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserActivityLogger {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserActivityLogger(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void logAllActivities() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            for (String key : keys) {
                Object value = redisTemplate.opsForValue().get(key);
                System.out.println("Key: " + key + " Value: " + value);
            }
        }
    }
}
