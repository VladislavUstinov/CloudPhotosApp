package com.laserdiffraction01.laserdiffraction01.bootstrap;


import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
public class LaserDiffractionBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;

    public LaserDiffractionBootStrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        userRepository.saveAll(getUsers());

        log.debug("LaserDiffractionBootStrap.onApplicationEvent()");

        log.debug("userRepository.count() = " + userRepository.count());
        log.debug("Have successfully loaded Bootstrap Data - Users");
    }

    private List<User> getUsers() {

        log.debug("LaserDiffractionBootStrap.getUsers()");

        List<User> users = new ArrayList<>();
        users.add(new User("user1", "passw1", "passw1"));
        users.add(new User("user2", "passw2", "passw2"));
        users.add(new User("user3", "passw3", "passw3"));

        log.debug("users.size() = " + users.size());
        return users;
    }
}

