package com.apartment.onboarding.registration.bean.student;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Validated
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long collegeId;

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


}
