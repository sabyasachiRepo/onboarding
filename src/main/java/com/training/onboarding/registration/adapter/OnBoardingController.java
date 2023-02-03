package com.training.onboarding.registration.adapter;


import com.training.onboarding.registration.bean.InstituteResponse;
import com.training.onboarding.registration.bean.Institutes;
import com.training.onboarding.registration.bean.Role;
import com.training.onboarding.registration.bean.User;
import com.training.onboarding.registration.jwt.JwtConfig;
import com.training.onboarding.registration.ports.api.OnboardingDataPort;
import com.training.onboarding.registration.repo.UserRepository;
import com.training.onboarding.registration.service.user.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.onboarding.registration.utils.Util;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@Validated
@RequestMapping("/institute/api/v1/onboarding/")
public class OnBoardingController {

    private Logger logger= LoggerFactory.getLogger(OnBoardingController.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private OnboardingDataPort onboardingDataPort;

    @Autowired
    private Util util;

    @PostMapping("/registration")
    public ResponseEntity studentRegistration(@Valid @RequestBody User user, HttpServletRequest httpServletRequest) {
        onboardingDataPort.registerNewUser(user,httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (onboardingDataPort.validateVerificationCode(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            try {
                String refresh_token = authorizationHeader.substring(jwtConfig.getTokenPrefix().length());
                Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();

                User user = onboardingDataPort.getUserByEmail(username);
                String access_token = util.getAccessToken(request, algorithm, user);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                sendInvalidTokenResponse(response,exception.getMessage());
            }
        } else {
            throw new RuntimeException("Refresh token is missing");

        }
    }



    @PostMapping("/token/validate")
    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        logger.info("Received Auth header -> {}",authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            try {

                String authToken = authorizationHeader.substring(jwtConfig.getTokenPrefix().length());

                Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();

                DecodedJWT decodedJWT = verifier.verify(authToken);
                String username = decodedJWT.getSubject();
                User user = onboardingDataPort.getUserByEmail(username);

                Map<String, String> userdata = new HashMap<>();
                userdata.put("user_id", String.valueOf(user.getId()));
                userdata.put("user_role",String.valueOf(user.getRole().getName()));
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), userdata);
            }
            catch (TokenExpiredException exception){
                sendInvalidTokenResponse(response,"Token expired");

            }
            catch (JWTVerificationException exception) {
                sendInvalidTokenResponse(response,"Invalid token");
            }

        } else {
            sendInvalidTokenResponse(response,"Invalid token");
        }
    }

    private static void sendInvalidTokenResponse(HttpServletResponse response,String message) throws IOException {
        response.setHeader("message",message);
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", message);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    @GetMapping("/institutes")
    public @ResponseBody Institutes getInstitutes() {
        return onboardingDataPort.getInstitutes();
    }

}