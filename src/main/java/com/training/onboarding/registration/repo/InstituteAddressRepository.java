package com.training.onboarding.registration.repo;

import com.training.onboarding.registration.bean.InstituteAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface InstituteAddressRepository extends JpaRepository<InstituteAddress,Long> {

}
