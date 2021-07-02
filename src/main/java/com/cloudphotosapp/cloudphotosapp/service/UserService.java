package com.cloudphotosapp.cloudphotosapp.service;

import com.cloudphotosapp.cloudphotosapp.domain.User;
import com.cloudphotosapp.cloudphotosapp.DTO.UserChangePasswordDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public interface UserService extends UserDetailsService {

    //public void setPasswordEncoder (PasswordEncoder passwordEncoder);

    List<User> findAll ();

    @Transactional
    boolean updateUser(User userForm, String roleName);

    @Transactional
    boolean updateUser(User userForm, ArrayList<String> roleNames);

    public void deleteUserByUsername (String username) throws UsernameNotFoundException;

    public boolean checkPasswordsAndSave(UserChangePasswordDTO userForm);
}
