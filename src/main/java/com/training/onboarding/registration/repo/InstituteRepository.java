package com.training.onboarding.registration.repo;

import com.training.onboarding.registration.bean.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface InstituteRepository extends JpaRepository<Institute,Long> {

}
