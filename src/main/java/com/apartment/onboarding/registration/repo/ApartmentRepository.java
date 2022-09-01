package com.apartment.onboarding.registration.repo;

import com.apartment.onboarding.registration.bean.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface ApartmentRepository extends JpaRepository<Apartment,Long> {
}
