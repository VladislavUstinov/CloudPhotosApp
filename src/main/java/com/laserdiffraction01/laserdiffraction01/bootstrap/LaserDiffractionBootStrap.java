package com.laserdiffraction01.laserdiffraction01.bootstrap;


import com.laserdiffraction01.laserdiffraction01.domain.Role;
import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.RoleRepository;
import com.laserdiffraction01.laserdiffraction01.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
public class LaserDiffractionBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    //private final BCryptPasswordEncoder bCryptPasswordEncoder;
    //@Autowired
    //BCryptPasswordEncoder bCryptPasswordEncoder;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder (PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public LaserDiffractionBootStrap(UserRepository userRepository, RoleRepository roleRepository){//}, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        //this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Role userRole = new Role(1L, Role.USER_ROLE_STRING);
        Role adminRole = new Role(2L, Role.ADMIN_ROLE_STRING);

        userRepository.saveAll(getUsersWithRoleUser(userRole));
        roleRepository.save (userRole);

        userRepository.saveAll(getUsersWithRoleAdmin(adminRole));
        roleRepository.save (adminRole);

        log.debug("LaserDiffractionBootStrap.onApplicationEvent()");

        log.debug("userRepository.count() = " + userRepository.count());
        log.debug("Have successfully loaded Bootstrap Data - Users");
    }

    private List<User> getUsersWithRoleUser(Role userRole) {

        log.debug("LaserDiffractionBootStrap.getUsersWithRoleUser(Role userRole)");

        List<User> users = new ArrayList<>();
        users.add(new User("user1", "passw1", "passw1", userRole));
        users.add(new User("user2", "passw2", "passw2", userRole));
        users.add(new User("user3", "passw3", "passw3", userRole));

        log.debug("users.size() = " + users.size());

        userRole.setUsers(users);

        for (int i = 0; i < users.size(); i ++) {
            users.get(i).setPassword(passwordEncoder.encode(users.get(i).getPassword()));
            users.get(i).setPasswordConfirmation(passwordEncoder.encode(users.get(i).getPasswordConfirmation()));
        }

        return users;
    }

    private List<User> getUsersWithRoleAdmin(Role adminRole) {

        log.debug("LaserDiffractionBootStrap.getUsersWithRoleAdmin(Role adminRole)");

        List<User> users = new ArrayList<>();
        users.add(new User("admin", "admin", "admin", adminRole));

        log.debug("users.size() = " + users.size());

        adminRole.setUsers(users);

        for (int i = 0; i < users.size(); i ++) {
            users.get(i).setPassword(passwordEncoder.encode(users.get(i).getPassword()));
            users.get(i).setPasswordConfirmation(passwordEncoder.encode(users.get(i).getPasswordConfirmation()));
        }

        return users;
    }
}

