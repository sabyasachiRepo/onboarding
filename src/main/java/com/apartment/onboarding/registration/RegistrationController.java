package com.apartment.onboarding.registration;


import com.apartment.onboarding.registration.bean.Apartment;
import com.apartment.onboarding.registration.bean.student.Student;
import com.apartment.onboarding.registration.repo.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class RegistrationController {

    @Autowired
    private ApartmentRepository apartmentRegistrationRepository;

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello";
    }

    @PostMapping("/register-apartment")
    public ResponseEntity apartmentRegistration(@Valid @RequestBody Apartment apartment){
        apartment.getApartmentAddress().setApartment(apartment);
        apartmentRegistrationRepository.save(apartment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }





}
