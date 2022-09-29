package com.training.onboarding.registration.repo;

import com.training.onboarding.registration.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT s FROM User s WHERE s.verificationCode = ?1")
    public User findByVerificationCode(String code);

    User findByEmail(String email);
}
