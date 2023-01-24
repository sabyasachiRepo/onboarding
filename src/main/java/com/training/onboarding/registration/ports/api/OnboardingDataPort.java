package com.training.onboarding.registration.ports.api;

import com.training.onboarding.registration.bean.InstituteResponse;
import com.training.onboarding.registration.bean.Institutes;
import com.training.onboarding.registration.bean.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OnboardingDataPort {
    Institutes getInstitutes();
    void registerNewUser(User user, HttpServletRequest httpServletRequest);

    boolean validateVerificationCode(String code);

    User getUserByEmail(String userEmail);

}
