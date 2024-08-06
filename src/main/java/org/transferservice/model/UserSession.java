package org.transferservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@RedisHash("UserSession")
public class UserSession implements Serializable {

    @Id
    private String sessionId;
    private String email;
    private String activityType;
    private String httpMethod;
    private String parameters;
    private Instant timestamp;

    public UserSession(String sessionId, String email, String activityType, String httpMethod, String parameters, Instant timestamp) {
        this.sessionId = sessionId;
        this.email = email;
        this.activityType = activityType;
        this.httpMethod = httpMethod;
        this.parameters = parameters;
        this.timestamp = timestamp;
    }

    public UserSession() {
        // Default constructor
    }
}
