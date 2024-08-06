package org.transferservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.transferservice.model.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByNationalIdNumber(String nationalIdNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Customer> findUserByEmail(String email);
}
