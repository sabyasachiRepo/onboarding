package com.training.onboarding.registration.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.training.onboarding.registration.bean.Role;
import com.training.onboarding.registration.bean.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Util {

    public  String getAccessToken(HttpServletRequest request, Algorithm algorithm, User user) {
        List<Role> roleList = new ArrayList<>();
        roleList.add(user.getRole());
        String access_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", roleList)
                .sign(algorithm);
        return access_token;
    }

}
