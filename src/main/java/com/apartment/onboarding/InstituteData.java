package com.apartment.onboarding;

import com.apartment.onboarding.course.Course;
import com.apartment.onboarding.registration.bean.Institute;
import com.apartment.onboarding.registration.bean.InstituteAddress;
import com.apartment.onboarding.registration.bean.Role;
import com.apartment.onboarding.registration.repo.CourseRepository;
import com.apartment.onboarding.registration.repo.InstituteAddressRepository;
import com.apartment.onboarding.registration.repo.InstituteRepository;
import com.apartment.onboarding.registration.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InstituteData implements CommandLineRunner {

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private InstituteAddressRepository instituteAddressRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CourseRepository courseRepository;



    @Override
    public void run(String... args) throws Exception {
        //saveData();


    }

    private void saveData() {
        InstituteAddress instituteAddress1=new InstituteAddress();
        instituteAddress1.setAddressLine1("Ramamurthy Nagar");
        instituteAddress1.setAddressLine2("Kalakare Main Road");
        instituteAddress1.setAddressLine3("Near Check post");
        instituteAddress1.setPin("438735");

        InstituteAddress instituteAddress2=new InstituteAddress();
        instituteAddress2.setAddressLine1("Malviya Nagar");
        instituteAddress2.setAddressLine2("Huaz Khas Main Road");
        instituteAddress2.setAddressLine3("Near Petrol post");
        instituteAddress2.setPin("823749");

        InstituteAddress instituteAddress3=new InstituteAddress();
        instituteAddress3.setAddressLine1("Chattarpur Nagar");
        instituteAddress3.setAddressLine2("Gurgaon Main Road");
        instituteAddress3.setAddressLine3("Near Dala street");
        instituteAddress3.setPin("539484");

        instituteAddressRepository.save(instituteAddress1);
        instituteAddressRepository.save(instituteAddress2);
        instituteAddressRepository.save(instituteAddress3);


        Institute institute1=new Institute();
        institute1.setName("CV Raman institute of technology");
        institute1.setInstituteAddress(instituteAddress1);

        Institute institute2=new Institute();
        institute2.setName("Seemanta Engg. College");
        institute2.setInstituteAddress(instituteAddress2);

        Institute institute3=new Institute();
        institute3.setName("Thompson Tech. University");
        institute3.setInstituteAddress(instituteAddress3);


        instituteRepository.save(institute1);
        instituteRepository.save(institute2);
        instituteRepository.save(institute3);


        Role role1=new Role();
        role1.setName("ROLE_ADMIN");

        Role role2=new Role();
        role2.setName("ROLE_INSTITUTE_ADMIN");

        Role role3=new Role();
        role3.setName("ROLE_STUDENT");

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);



        Course course1=new Course();
        course1.setName("Java");

        Course course2=new Course();
        course2.setName("Python");

        Course course3=new Course();
        course3.setName("C++");

        Course course4=new Course();
        course4.setName("Java Script");

        Course course5=new Course();
        course5.setName("AWS");

        Course course6=new Course();
        course6.setName("Software Testing");

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);
        courseRepository.save(course5);
        courseRepository.save(course6);
    }
}
