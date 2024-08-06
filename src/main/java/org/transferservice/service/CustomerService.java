package org.transferservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.UpdateCustomerDTO;
import org.transferservice.dto.UpdateProfileDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Customer;
import org.transferservice.repository.CustomerRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements ICustomer {

    private final CustomerRepository customerRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Customer updateProfile(Long customerId, UpdateProfileDTO updateProfileDTO) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));


        if (updateProfileDTO.getFirstName() != null && !updateProfileDTO.getFirstName().isEmpty()) {
            customer.setFirstName(updateProfileDTO.getFirstName());
        }
        if (updateProfileDTO.getLastName() != null && !updateProfileDTO.getLastName().isEmpty()) {
            customer.setLastName(updateProfileDTO.getLastName());
        }
        if (updateProfileDTO.getPhone() != null && !updateProfileDTO.getPhone().isEmpty()) {
            customer.setPhoneNumber(updateProfileDTO.getPhone());
        }
        if (updateProfileDTO.getEmail() != null && !updateProfileDTO.getEmail().isEmpty()) {
            customer.setEmail(updateProfileDTO.getEmail());
        }

        return customerRepository.save(customer);
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Customer updateCustomer(Long id, UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException {

        Customer customer = this.getCustomerByCustomerId(id);

        customer.setFirstName(updateCustomerDTO.getFirstName());
        customer.setLastName(updateCustomerDTO.getLastName());
        customer.setEmail(updateCustomerDTO.getEmail());
        customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
        customer.setAddress(updateCustomerDTO.getAddress());
        customer.setNationality(updateCustomerDTO.getNationality());
        customer.setNationalIdNumber(updateCustomerDTO.getNationalIdNumber());
        customer.setDateOfBirth(updateCustomerDTO.getDateOfBirth());

        return this.customerRepository.save(customer);

    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {

        Customer customer = this.getCustomerByCustomerId(id);

        this.customerRepository.delete(customer);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException {

        Customer customer = this.getCustomerByCustomerId(id);

        return customer.toDTO();
    }

    private Customer getCustomerByCustomerId(Long id) throws CustomerNotFoundException {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with Id %s not found", id)));
    }

}
