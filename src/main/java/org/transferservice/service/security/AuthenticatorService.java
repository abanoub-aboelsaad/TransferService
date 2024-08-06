package org.transferservice.service.security;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.AccountType;
import org.transferservice.exception.custom.CustomerAlreadyExistException;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Account;
import org.transferservice.model.Customer;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.CustomerRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthenticatorService implements IAuthenticator {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    @Override
    @Transactional
    public CustomerDTO register(CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException {

        if (this.customerRepository.existsByEmail(createCustomerDTO.getEmail())) {
            throw new CustomerAlreadyExistException(String.format("Customer with email %s already exists", createCustomerDTO.getEmail()));
        }

        if (this.customerRepository.existsByNationalIdNumber(createCustomerDTO.getNationalIdNumber())) {
            throw new CustomerAlreadyExistException(String.format("Customer with National Id Number %s already exists", createCustomerDTO.getNationalIdNumber()));
        }

        if (this.customerRepository.existsByPhoneNumber(createCustomerDTO.getPhoneNumber())) {
            throw new CustomerAlreadyExistException(String.format("Customer with phone number %s already exists", createCustomerDTO.getPhoneNumber()));
        }


        Account account = Account.builder()
                .accountNumber(String.valueOf(new SecureRandom().nextInt(1000000000)))
                .accountType(AccountType.SAVINGS)
                .accountName(createCustomerDTO.getFirstName() + " " + createCustomerDTO.getLastName())
                .accountDescription("Savings Account")
                .active(true)
                .currency(AccountCurrency.EGP)
                .balance(0.0)
                .build();


        Customer customer = Customer
                .builder()
                .email(createCustomerDTO.getEmail())
                .firstName(createCustomerDTO.getFirstName())
                .lastName(createCustomerDTO.getLastName())
                .phoneNumber(createCustomerDTO.getPhoneNumber())
                .address(createCustomerDTO.getAddress())
                .gender(createCustomerDTO.getGender())
                .nationalIdNumber(createCustomerDTO.getNationalIdNumber())
                .dateOfBirth(createCustomerDTO.getDateOfBirth())
                .nationality(createCustomerDTO.getNationality())
                .password(this.encoder.encode(createCustomerDTO.getPassword()))
                .account(this.accountRepository.save(account))
                .build();

        return this.customerRepository.save(customer).toDTO();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return LoginResponseDTO.builder()
                .token(jwt)
                .message("Login Successful")
                .status(HttpStatus.ACCEPTED)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void logout(String token) {
        jwtUtils.invalidateJwtToken(token);
        SecurityContextHolder.clearContext();
    }
    @Override
    @Transactional
    public void updatePassword(String email, String oldPassword, String newPassword) throws CustomerNotFoundException {
        // Validate password strength (optional)
        validatePasswordStrength(newPassword);

        Customer customer = customerRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with email %s not found", email)));

        if (!encoder.matches(oldPassword, customer.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (encoder.matches(newPassword, customer.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        customer.setPassword(encoder.encode(newPassword));
        customerRepository.save(customer);
    }

    private void validatePasswordStrength(String password) {
        // Example password strength validation
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        // Add more validations as needed (e.g., uppercase, numbers, special characters)
    }
}
