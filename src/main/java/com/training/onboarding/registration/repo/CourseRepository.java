package com.training.onboarding.registration.repo;

import com.training.onboarding.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface CourseRepository extends JpaRepository<Course,Long> {


}
