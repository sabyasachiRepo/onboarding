package com.apartment.onboarding.registration.repo;

import com.apartment.onboarding.registration.bean.Institute;
import com.apartment.onboarding.registration.bean.InstituteAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface InstituteAddressRepository extends JpaRepository<InstituteAddress,Long> {

}
