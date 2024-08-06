package org.transferservice.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.AccountDTO;
import org.transferservice.model.Account;
import org.transferservice.service.AccountService;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@Validated
public class AccountController {


    @Autowired
    private AccountService accountService;


    @PostMapping("/createAccount")
    public ResponseEntity<Account> createAccount(@RequestBody @Valid Account account) {

        Account createdAccount = accountService.createAccount(account);

        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);

    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getAccountBalance(@RequestParam Long accountId) {
        Double balance = accountService.getAccountBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(value -> new ResponseEntity<>(convertToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private AccountDTO convertToDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .accountName(account.getAccountName())
                .accountDescription(account.getAccountDescription())
                .active(account.getActive())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account accountDetails) {
        Optional<Account> existingAccount = accountService.getAccountById(id);
        if (existingAccount.isPresent()) {
            Account account = existingAccount.get();

            account.setAccountName(accountDetails.getAccountName());
            account.setAccountDescription(accountDetails.getAccountDescription());
            account.setBalance(accountDetails.getBalance());
            account.setAccountType(accountDetails.getAccountType());
            account.setCurrency(accountDetails.getCurrency());
            account.setActive(accountDetails.getActive());

            Account updatedAccount = accountService.updateAccount(account);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        if (account.isPresent()) {

            accountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}