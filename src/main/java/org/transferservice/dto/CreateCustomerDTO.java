package org.transferservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;


@Data
@Builder
public class CreateCustomerDTO {

    private final Long id;

    @NotNull
    private final String firstName;
    @NotNull
    private final String lastName;

    @NotNull
    @Email
    private final String email;

    @NotNull
    private final String phoneNumber;

    @NotNull
    private final String address;

    @NotNull
    private final String nationality;

    @NotNull
    @Size(max = 14)
    private final String nationalIdNumber;

    @NotNull
    private final Gender gender;

    @NotNull
    private final LocalDate dateOfBirth;

    @NotNull
    @Size(min = 6)
    private final String password;

}
