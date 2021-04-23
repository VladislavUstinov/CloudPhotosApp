package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    // todo where should I put @Transactional

    @Transactional
    @Override
    public List<User> findAll() {
        log.debug("I'm in the UserServiceImpl - findAll()");

        System.out.println ("for through userRepository.findAll().iterator() in UserService.findAll()");

        for ( Iterator<User> userIterator = userRepository.findAll().iterator() ;
              userIterator.hasNext(); ) {

            User user = userIterator.next();
            System.out.println (user.getName());
        }

        List<User> userList = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(userList::add);

        return userList;
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
