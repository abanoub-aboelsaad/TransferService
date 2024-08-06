package org.transferservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * This component listens for the application context refresh event and checks
 * the connection to Redis.
 */
@Component
public class RedisConnectionCheckListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RedisConnectionCheckListener.class);

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * Invoked when the application context is refreshed. This method
     * checks the Redis connection.
     *
     * @param event the context refreshed event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Test the Redis connection
        testConnection();
    }

    /**
     * Tests the connection to Redis by sending a PING command.
     */
    private void testConnection() {
        RedisConnection connection = null;
        try {
            connection = redisConnectionFactory.getConnection();
            // Execute a simple PING command to check the connection
            String response = connection.ping();
            if ("PONG".equals(response)) {
                logger.info("Connected to Redis successfully.");
            } else {
                logger.warn("Failed to connect to Redis. Response: {}", response);
            }
        } catch (Exception e) {
            logger.error("Error connecting to Redis: {}", e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    logger.error("Error closing Redis connection: {}", e.getMessage(), e);
                }
            }
        }
    }
}
