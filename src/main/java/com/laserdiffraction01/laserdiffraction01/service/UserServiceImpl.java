package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.Role;
import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.RoleRepository;
import com.laserdiffraction01.laserdiffraction01.repository.UserRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder (PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // todo where should I put @Transactional


    @Transactional
    @Override
    public List<User> findAll() {
        log.debug("I'm in the UserServiceImpl - findAll()");

        //todo - do I really need to make deep copy? Although they are only references, it's not that expensive..
        List<User> userList = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(userList::add);

        return userList;
    }

    @Transactional
    @Override
    public boolean saveUser(User userForm, String roleName) {
        ArrayList<String> roleNames = new ArrayList<>();
        roleNames.add (roleName);
        return saveUser(userForm, roleNames);
    }

    @Transactional
    @Override
    public boolean saveUser(User userForm, ArrayList<String> roleNames) {
        if (userForm == null)
            return false;

        if (userRepository.findByUsername(userForm.getUsername()) != null)
            return false;

        //todo может переставить это дело в контроллер?
        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userForm.setPasswordConfirmation(passwordEncoder.encode(userForm.getPasswordConfirmation()));

        if (userForm.getRoles() == null)
            userForm.setRoles (new HashSet<>());

        for (int i = 0; i < roleNames.size(); i ++) {
            Role role = roleRepository.findByName(roleNames.get(i));
            if (role != null) {
                role.getUsers().add(userForm);
                userForm.getRoles().add (role);
            } else
                log.debug("in UserServiceImpl.saveUser(...): roleRepository.findByName(" +
                        roleNames.get(i) + ") returned null");
        }


        if (userRepository.save(userForm).equals(userForm))
            return true;

        return false;
    }

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User {" + username + "} not found");
        }

        return user;
    }
}




/*        System.out.println ("for through userRepository.findAll().iterator() in UserService.findAll()");

        for ( Iterator<User> userIterator = userRepository.findAll().iterator() ;
              userIterator.hasNext(); ) {

            User user = userIterator.next();
            System.out.println (user.getName());
        }*/