package com.apartment.onboarding.registration.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;


@NoArgsConstructor
@Getter
@Setter
@Validated
@Entity
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String apartmentName;

    @OneToOne(mappedBy = "apartment",cascade = CascadeType.ALL)
    private @Valid ApartmentAddress apartmentAddress;


/*
    @OneToOne(mappedBy = "id")
    private ApartmentContact apartmentContact1;

    @OneToOne(mappedBy = "id")
    private ApartmentContact apartmentContact2;

    @OneToOne(mappedBy = "id")
    private ApartmentContact apartmentContact3;*/



}
