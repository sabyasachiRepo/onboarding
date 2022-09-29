package com.training.onboarding.registration.ports.spi;

import com.training.onboarding.registration.bean.InstituteResponse;
import com.training.onboarding.registration.bean.User;

import java.util.List;

public interface OnboardingPersistencePort {
    List<InstituteResponse> getInstitutes();
    void saveUser(User user);
    void addRoleToUser(String userEmail,String role);

    User findUserByVerificationCode(String code);

    User getUserByEmail(String userEmail);
}
