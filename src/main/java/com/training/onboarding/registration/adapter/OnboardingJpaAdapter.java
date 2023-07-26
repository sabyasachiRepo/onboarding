package com.training.onboarding.registration.adapter;

import com.training.onboarding.registration.bean.InstituteResponse;
import com.training.onboarding.registration.bean.Role;
import com.training.onboarding.registration.bean.User;
import com.training.onboarding.registration.ports.spi.OnboardingPersistencePort;
import com.training.onboarding.registration.repo.InstituteRepository;
import com.training.onboarding.registration.repo.OnboardingMapper;
import com.training.onboarding.registration.repo.RoleRepository;
import com.training.onboarding.registration.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OnboardingJpaAdapter implements OnboardingPersistencePort {

    private Logger logger = LoggerFactory.getLogger(OnboardingJpaAdapter.class);


    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private OnboardingMapper onboardingMapper;


    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;

    @Override
    public List<InstituteResponse> getInstitutes() {
        return onboardingMapper.fromEntity(instituteRepository.findAll());
    }

    @Override
    public void saveUser(User user) {
       userRepo.save(user);
    }

    @Override
    public void addRoleToUser(String userEmail, String roleName) {
        User user=userRepo.findByEmail(userEmail);
        logger.info("User info we got for "+userEmail+" is " +user);
        Role role=roleRepo.findByName(roleName);
        logger.info("Role info we got for "+roleName+" is " +role);

        user.setRole(role);
        userRepo.save(user);
    }

    @Override
    public User findUserByVerificationCode(String code) {
        return userRepo.findByVerificationCode(code);
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepo.findByEmail(userEmail);
    }
}
