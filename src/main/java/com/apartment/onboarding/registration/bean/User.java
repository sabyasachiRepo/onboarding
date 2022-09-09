package com.apartment.onboarding.registration.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long instituteId;

    @NotBlank(message = "FirstName can not be blank")
    private String firstName;

    @NotBlank(message = "LastName can not be blank")
    private String lastName;

    @NotBlank(message = "Email can not be blank")
    private String email;

    @NotBlank(message = "Phone Number can not be blank")
    private String phoneNumber;

    @NotBlank(message = "Password can not be blank")
    private String password;

    private String verificationCode;

    private Boolean isEnabled;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

}
