package com.training.onboarding.registration.service.user;

import com.training.onboarding.registration.bean.InstituteResponse;
import com.training.onboarding.registration.bean.Role;
import com.training.onboarding.registration.bean.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String userName,String roleName);
    User getUser(String userName);
    List<User> getUsers();

    List<InstituteResponse> getInstitutes();

}

