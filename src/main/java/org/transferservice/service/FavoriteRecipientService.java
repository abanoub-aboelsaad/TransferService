package org.transferservice.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.FavoriteRecipientDTO;
import org.transferservice.model.Customer;
import org.transferservice.model.FavoriteRecipient;
import org.transferservice.repository.CustomerRepository;
import org.transferservice.repository.FavoriteRecipientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoriteRecipientService {

    private FavoriteRecipientRepository favoriteRecipientRepository;

    private final CustomerRepository customerRepository;




    @Transactional
    public FavoriteRecipient addFavoriteRecipient(FavoriteRecipientDTO favoriteRecipientDTO , Long customerId) {

        String recipientName = favoriteRecipientDTO.getFirstName() + " " + favoriteRecipientDTO.getLastName();


        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        FavoriteRecipient favoriteRecipient = FavoriteRecipient.builder()
                .recipientName(recipientName)
                .recipientAccountNumber(favoriteRecipientDTO.getRecipientAccountNumber())
                .customer(customer)
                .build();

        return favoriteRecipientRepository.save(favoriteRecipient);
    }

//    public List<FavoriteRecipient> getFavoriteRecipientsByCustomerId(Long customerId) {
//        return favoriteRecipientRepository.findByCustomerId(customerId);
//    }

    public List<FavoriteRecipientDTO> getFavoriteRecipientsByCustomerId(Long customerId) {
        List<FavoriteRecipient> favoriteRecipients = favoriteRecipientRepository.findByCustomerId(customerId);

        return favoriteRecipients.stream()
                .map(recipient -> {
                    String[] nameParts = recipient.getRecipientName().split(" ", 2);
                    String firstName = nameParts[0];
                    String lastName = nameParts.length > 1 ? nameParts[1] : "";
                    return FavoriteRecipientDTO.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .recipientAccountNumber(recipient.getRecipientAccountNumber())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Optional<FavoriteRecipient> getFavoriteRecipientByCustomerIdAndAccountNumber(Long customerId, String accountNumber) {
        return favoriteRecipientRepository.findByCustomerIdAndRecipientAccountNumber(customerId, accountNumber);
    }

    @Transactional
    public void removeFavoriteRecipient(Long id) {
        favoriteRecipientRepository.deleteById(id);
    }



}
