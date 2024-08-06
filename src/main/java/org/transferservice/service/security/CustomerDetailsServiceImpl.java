package org.transferservice.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.model.Customer;
import org.transferservice.repository.CustomerRepository;


@Service
@RequiredArgsConstructor
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer Not Found with email: " + username));

        return CustomerDetailsImpl.builder()
                .email(customer.getEmail())
                .password(customer.getPassword()).build();
    }
}
