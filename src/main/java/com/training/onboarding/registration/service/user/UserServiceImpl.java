package com.training.onboarding.registration.service.user;


import com.training.onboarding.registration.bean.Institute;
import com.training.onboarding.registration.bean.InstituteResponse;
import com.training.onboarding.registration.bean.Role;
import com.training.onboarding.registration.bean.User;
import com.training.onboarding.registration.repo.InstituteRepository;
import com.training.onboarding.registration.repo.RoleRepository;
import com.training.onboarding.registration.repo.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service @Transactional
public class UserServiceImpl implements  UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private InstituteRepository instituteRepository;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepo.findByEmail(email);

        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority( user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);

    }
    @Override
    public User saveUser(User user) {
       // log.info("Saving new user {} to the database",user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
       // log.info("Saving new role {} to the database",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
      //  log.info("Adding role {} to user {}",roleName,userName);
        User user=userRepo.findByEmail(email);
        Role role=roleRepo.findByName(roleName);
        user.setRole(role);

    }

    @Override
    public User getUser(String email) {
      //  log.info("Fetching user {}",userName);
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        //log.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override
    public List<InstituteResponse> getInstitutes() {
        List<Institute> instituteEntities = instituteRepository.findAll();

        List<InstituteResponse> instituteResponseList =new ArrayList<>(instituteEntities.size());
        for(Institute institute : instituteEntities){
            InstituteResponse instituteResponse =new InstituteResponse();
            BeanUtils.copyProperties(institute, instituteResponse);
            instituteResponseList.add(instituteResponse);
        }
        return instituteResponseList;
    }

}
