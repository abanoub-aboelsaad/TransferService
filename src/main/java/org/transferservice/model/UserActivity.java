package org.transferservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_activities")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private String email;
    private String activityType;
    private String httpMethod;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    public UserActivity(String sessionId, String email, String activityType, String httpMethod, LocalDateTime timestamp) {
        this.sessionId = sessionId;
        this.email = email;
        this.activityType = activityType;
        this.httpMethod = httpMethod;
        this.timestamp = timestamp;
    }

}
