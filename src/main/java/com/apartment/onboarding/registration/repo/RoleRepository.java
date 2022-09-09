package com.apartment.onboarding.registration.repo;

import com.apartment.onboarding.registration.bean.Role;
import com.apartment.onboarding.registration.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);

}
