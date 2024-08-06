package org.transferservice.service;

import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.UpdateCustomerDTO;
import org.transferservice.dto.UpdateProfileDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Customer;


public interface ICustomer {

    /**
     * Update customer details
     *
     * @param id                customer id
     * @param updateCustomerDTO customer details
     * @return updated customer @{@link Customer}
     * @throws CustomerNotFoundException if customer not found
     */
    Customer updateCustomer(Long id, UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException;


    /**
     * Delete customer
     *
     * @param id customer id
     * @throws CustomerNotFoundException if customer not found
     */
    void deleteCustomer(Long id) throws CustomerNotFoundException;

    /**
     * Get customer by id
     *
     * @param id customer id
     * @return customer details @{@link CustomerDTO}
     * @throws CustomerNotFoundException if customer not found
     */
    CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException;

    Customer updateProfile(Long customerId, UpdateProfileDTO updateProfileDTO) throws CustomerNotFoundException;
}
