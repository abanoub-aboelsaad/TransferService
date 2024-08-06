package org.transferservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.transferservice.model.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession, String> {
    // Custom query methods can be added if necessary
}
