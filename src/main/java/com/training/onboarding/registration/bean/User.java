package com.training.onboarding.registration.bean;


import com.training.onboarding.course.Course;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @OneToOne(fetch = FetchType.EAGER)
    private Role role;

    @ManyToMany
    @JoinTable(name = "USER_COURSE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "COURSE_ID")
    )
    private List<Course> courses;

}
