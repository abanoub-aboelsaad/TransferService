package org.transferservice.service;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.transferservice.model.Account;
import org.transferservice.repository.AccountRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {

        return accountRepository.save(account);

    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Double getAccountBalance(Long id){

        Optional<Account> accountopt = accountRepository.findById(id);

        if(accountopt.isPresent()){
            return accountopt.get().getBalance();
        }
        else {
            throw new IllegalArgumentException("Account not found");
        }

    }

    public Account updateAccount(Account account) {

        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {

        accountRepository.deleteById(id);

    }






}
