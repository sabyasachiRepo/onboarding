package com.apartment.onboarding;


import com.apartment.onboarding.registration.bean.Role;
import com.apartment.onboarding.registration.bean.User;
import com.apartment.onboarding.registration.jwt.JwtConfig;
import com.apartment.onboarding.registration.repo.UserRepository;
import com.apartment.onboarding.registration.service.user.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

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
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("/registration")
    public ResponseEntity studentRegistration(@Valid @RequestBody User user, HttpServletRequest httpServletRequest) {
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setIsEnabled(true);
        userService.saveUser(user);
        userService.addRoleToUser(user.getEmail(), "ROLE_STUDENT");

        try {
            // sendEmail(user,getSiteURL(httpServletRequest));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (verify(code)) {
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
                User user = userService.getUser(username);
                List<Role> roleList = new ArrayList<>();
                roleList.add(user.getRole());
                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", roleList)
                        .sign(algorithm);


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

    private void sendEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = "sabyasachi.biswal1@gmail.com";
        String fromAddress = "sabyasachi.biswal1@gmail.com";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.getIsEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setIsEnabled(true);
            userRepository.save(user);
            return true;
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
                User user = userService.getUser(username);

                Map<String, String> userdata = new HashMap<>();
                userdata.put("user_id", String.valueOf(user.getId()));
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
}