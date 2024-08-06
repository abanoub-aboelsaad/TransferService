package org.transferservice.service.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.transferservice.model.UserActivity;
import org.transferservice.model.UserSession;
import org.transferservice.repository.UserActivityRepository;
import org.transferservice.repository.UserSessionRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityService.class);
    private static final long REDIS_EXPIRY_DURATION = 30; // 30 minutes
    private final UserActivityRepository userActivityRepository;
    private final UserSessionRepository userSessionRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void recordActivity(String sessionId, String email, String activityType, String httpMethod, String parameters) {
        Instant now = Instant.now();
        UserSession session = new UserSession(sessionId, email, activityType, httpMethod, parameters, now);

        UserActivity activity = new UserActivity(
                sessionId,
                email,
                activityType,
                httpMethod,
                LocalDateTime.now()
        );

        userActivityRepository.save(activity);
        logger.info("Activity recorded in database : Session ID: {}, Email: {}, Activity: {}", sessionId, email, activityType);
        // Save the session to Redis
        redisTemplate.opsForValue().set(sessionId, session, REDIS_EXPIRY_DURATION, TimeUnit.MINUTES);

        // Save the session to the database (if needed for persistence beyond Redis)
        userSessionRepository.save(session);

        logger.info("Activity recorded in redis: Session ID: {}, Email: {}, Activity: {}", sessionId, email, activityType);
    }

    public void checkSessionExpiration(String sessionId) {
        // Check if the session exists and get its TTL (Time To Live)
        Long ttl = redisTemplate.getExpire(sessionId, TimeUnit.MINUTES);
        if (ttl != null) {
            if (ttl <= 0) {
                // Session has expired
                userSessionRepository.deleteById(sessionId);
                redisTemplate.delete(sessionId); // Remove from Redis as well
                logger.info("Session expired and removed: Session ID: {}", sessionId);
            } else {
                logger.info("Session TTL checked: Session ID: {}, TTL (minutes): {}", sessionId, ttl);
            }
        } else {
            logger.warn("Unable to check TTL for Session ID: {}", sessionId);
        }
    }
}
