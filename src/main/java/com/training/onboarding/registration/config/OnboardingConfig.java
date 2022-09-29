package com.training.onboarding.registration.config;

import com.training.onboarding.registration.adapter.OnboardingJpaAdapter;
import com.training.onboarding.registration.ports.api.OnboardingDataPort;
import com.training.onboarding.registration.ports.spi.OnboardingPersistencePort;
import com.training.onboarding.registration.service.user.OnboardingDataPortImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class OnboardingConfig {

    @Bean
    public OnboardingPersistencePort onboardingPersistencePort(){
        return new OnboardingJpaAdapter();
    }

    @Bean
    public OnboardingDataPort onboardingDataPort(PasswordEncoder passwordEncoder, JavaMailSender javaMailSender){
        return new OnboardingDataPortImpl(onboardingPersistencePort(),passwordEncoder,javaMailSender);
    }

}
