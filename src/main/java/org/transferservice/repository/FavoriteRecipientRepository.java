package org.transferservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.transferservice.model.FavoriteRecipient;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRecipientRepository extends JpaRepository<FavoriteRecipient, Long> {

    List<FavoriteRecipient> findByCustomerId(Long customerId);

    Optional<FavoriteRecipient> findByCustomerIdAndRecipientAccountNumber(Long customerId, String accountNumber);



}
