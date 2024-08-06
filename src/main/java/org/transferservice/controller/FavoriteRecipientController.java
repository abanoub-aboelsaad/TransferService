package org.transferservice.controller;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.FavoriteRecipientDTO;
import org.transferservice.model.FavoriteRecipient;
import org.transferservice.service.FavoriteRecipientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
@Validated
@Data
public class FavoriteRecipientController {


    private  FavoriteRecipientService favoriteRecipientService;



    @Autowired
    public FavoriteRecipientController(FavoriteRecipientService favoriteRecipientService) {
        this.favoriteRecipientService = favoriteRecipientService;
    }


    @PostMapping("/user/{customerId}")
    public ResponseEntity<FavoriteRecipient> addFavoriteRecipient(
            @PathVariable Long customerId, @RequestBody FavoriteRecipientDTO favoriteRecipientDTO) {
        FavoriteRecipient createdFavoriteRecipient = favoriteRecipientService.addFavoriteRecipient(favoriteRecipientDTO, customerId);
        return new ResponseEntity<>(createdFavoriteRecipient, HttpStatus.CREATED);
    }


    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<FavoriteRecipientDTO>> getFavoriteRecipientsByCustomerId(@PathVariable Long customerId) {
        List<FavoriteRecipientDTO> favoriteRecipients = favoriteRecipientService.getFavoriteRecipientsByCustomerId(customerId);
        return new ResponseEntity<>(favoriteRecipients, HttpStatus.OK);
    }


    @GetMapping("/user/{customerId}/recipient/{accountNumber}")
    public ResponseEntity<FavoriteRecipient> getFavoriteRecipientByCustomerIdAndAccountNumber(
            @PathVariable Long customerId, @PathVariable String accountNumber) {
        Optional<FavoriteRecipient> favoriteRecipient = favoriteRecipientService.getFavoriteRecipientByCustomerIdAndAccountNumber(customerId, accountNumber);
        return favoriteRecipient.map(recipient -> new ResponseEntity<>(recipient, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFavoriteRecipient(@PathVariable Long id) {
        favoriteRecipientService.removeFavoriteRecipient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
