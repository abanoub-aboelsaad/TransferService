package org.transferservice.service.security;

import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.exception.custom.CustomerAlreadyExistException;
import org.transferservice.exception.custom.CustomerNotFoundException;

public interface IAuthenticator {

    /**
     * Register a new customer
     *
     * @param createCustomerDTO customer details
     * @return registered customer @{@link CustomerDTO}
     * @throws CustomerAlreadyExistException if customer already exist
     */
    CustomerDTO register(CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException;

    /**
     * Login a customer
     *
     * @param loginRequestDTO login details
     * @return login response @{@link LoginResponseDTO}
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    void logout(String token);


    @Transactional
    void updatePassword(String email, String oldPassword, String newPassword) throws CustomerNotFoundException;
}
