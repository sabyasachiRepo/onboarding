package com.apartment.onboarding.registration.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Validated
@Entity
@NoArgsConstructor
public class ApartmentAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;

    @NotNull(message = "pin can not be null")
    @Size(min = 6,max = 6 ,message = "pin should be of six digits")
    private String pin;
    private double latitude;
    private double longitude;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;
}
