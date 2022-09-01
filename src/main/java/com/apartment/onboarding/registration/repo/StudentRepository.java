package com.apartment.onboarding.registration.repo;

import com.apartment.onboarding.registration.bean.Apartment;
import com.apartment.onboarding.registration.bean.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface StudentRepository extends JpaRepository<Student,Long> {
    @Query("SELECT s FROM Student s WHERE s.verificationCode = ?1")
    public Student findByVerificationCode(String code);
}
