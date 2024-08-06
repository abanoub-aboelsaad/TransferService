package org.transferservice.service.security.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCustomerDTO {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final String address;
    private final String nationality;
    private final String nationalIdNumber;
    private final LocalDate dateOfBirth;
}
