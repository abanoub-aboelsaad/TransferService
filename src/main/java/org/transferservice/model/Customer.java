package org.transferservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false, unique = true)
    private String nationalIdNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime creationTimeStamp;

    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

    public CustomerDTO toDTO() {
        return CustomerDTO.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .gender(this.gender)
                .nationality(this.nationality)
                .nationalIdNumber(this.nationalIdNumber)
                .dateOfBirth(this.dateOfBirth)
//                .account(this.account.toDTO())
                .account(this.account != null ? this.account.toDTO() : null)
                .build();
    }
}
