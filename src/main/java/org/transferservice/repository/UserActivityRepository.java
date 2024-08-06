package org.transferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transferservice.model.UserActivity;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

}
