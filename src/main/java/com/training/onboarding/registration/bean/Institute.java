package com.training.onboarding.registration.bean;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Institute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Institute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(fetch = FetchType.EAGER)
    private InstituteAddress instituteAddress;
}
