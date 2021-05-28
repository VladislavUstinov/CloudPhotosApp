package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.User;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public interface UserService extends UserDetailsService {

    //public void setPasswordEncoder (PasswordEncoder passwordEncoder);

    List<User> findAll ();

    @Transactional
    boolean saveUser(User userForm, String roleName);

    @Transactional
    boolean saveUser(User userForm, ArrayList<String> roleNames);
}
