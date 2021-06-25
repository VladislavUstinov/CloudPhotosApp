package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.domain.UserChangePasswordDTO;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

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
