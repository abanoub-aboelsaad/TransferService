package org.transferservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.transferservice.model.UserSession;
import org.transferservice.repository.UserSessionRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;



@Service
@RequiredArgsConstructor
public class UserSessionService {

    private static final Logger logger = LoggerFactory.getLogger(UserSessionService.class);

    private static final long REDIS_EXPIRY_DURATION = 30; // 30 minutes
    private final UserSessionRepository userSessionRepository;


    private final RedisTemplate<String, Object> redisTemplate;

    public void recordActivity(String sessionId, String email, String activityType, String httpMethod, String parameters) {
        UserSession session = new UserSession(sessionId, email, activityType, httpMethod, parameters, Instant.now());

        // Save the session to Redis
        userSessionRepository.save(session);

        // Set expiration time for the session
        redisTemplate.opsForValue().set(sessionId, session, REDIS_EXPIRY_DURATION, TimeUnit.MINUTES);

        logger.info("Activity recorded: Session ID: {}, Email: {}, Activity: {}", sessionId, email, activityType);
    }

    public void checkSessionExpiration(String sessionId, long maxInactiveMinutes) {
        Optional<UserSession> session = userSessionRepository.findById(sessionId);
        session.ifPresent(userSession -> {
            long minutesSinceLastActivity = ChronoUnit.MINUTES.between(userSession.getTimestamp(), Instant.now());
            if (minutesSinceLastActivity > maxInactiveMinutes) {
                userSessionRepository.deleteById(sessionId);
                // Log the session expiration event
                logger.info("Session expired. SessionId: {}, Email: {}, LastActivity: {}, CurrentTime: {}",
                        sessionId, userSession.getEmail(), userSession.getTimestamp(), Instant.now());
                // Additional logic to handle session expiration (e.g., notify user, cleanup resources)
            }
        });
    }
}
