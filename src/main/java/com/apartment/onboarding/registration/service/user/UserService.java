package com.apartment.onboarding.registration.service.user;

import com.apartment.onboarding.registration.bean.Role;
import com.apartment.onboarding.registration.bean.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String userName,String roleName);
    User getUser(String userName);
    List<User> getUsers();

}

