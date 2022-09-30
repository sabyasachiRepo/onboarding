package com.training.onboarding.registration.security;

import com.training.onboarding.registration.jwt.JwtConfig;
import com.training.onboarding.registration.jwt.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.training.onboarding.registration.service.user.UserServiceImpl;
import com.training.onboarding.registration.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserServiceImpl applicationUserService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private Util util;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtUsernameAndPasswordAuthenticationFilter usernameAndPasswordAuthenticationFilter = new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig,util);
        usernameAndPasswordAuthenticationFilter.setFilterProcessesUrl("/institute/api/v1/onboarding/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
       /* http.authorizeRequests().antMatchers("/institute/api/v1/onboarding/login/**","/institute/api/v1/onboarding/token/**","/institute/api/v1/onboarding/registration/**","/").permitAll();
        http.authorizeRequests().antMatchers("/institute/api/v1/**","institute/api/v1/**").authenticated();
*/
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(usernameAndPasswordAuthenticationFilter);
       // http.addFilterAfter(new JwtTokenVerifierFilter(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class);

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // for decoding encoded password
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }



}
