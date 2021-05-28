package com.laserdiffraction01.laserdiffraction01.controller;

import com.laserdiffraction01.laserdiffraction01.domain.Role;
import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.RoleRepository;
import com.laserdiffraction01.laserdiffraction01.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import java.util.List;
import java.util.Set;


@Controller
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    ////////////////////   register new user   //////////////////
    @GetMapping("/user/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User("get", "get", "get"));

        return "user/registration";
    }

    /*@Valid*/
    @PostMapping("/user/registration")
    public String addUser(@ModelAttribute("userForm") User userForm, Model model) {

       /* if (bindingResult.hasErrors()) {
            return "user/registration";
        }*/
        if (!userForm.getPassword().equals(userForm.getPasswordConfirmation())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "user/registration";
        }
        if (!userService.saveUser(userForm, Role.USER_ROLE_STRING)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "user/registration";
        }

        return "redirect:/index";
    }

    ////////////////   login  ////////////////////
/*
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userForm", new User("get", "get", "get", roleRepository.findByName(Role.USER_ROLE_STRING)));

        return "login";
    }


    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("userForm") User userForm, Model model, BindingResult bindingResult) {

        UserDetails foundUserDetails = null;

        try {
            foundUserDetails = userService.loadUserByUsername(userForm.getUsername());
        } catch (UsernameNotFoundException exc) {
            //model.addAttribute("usernameError", "User with this name is not found, please try again");
            bindingResult.addError(new ObjectError("username", "User with this name is not found, please try again"));
            return "login";
        }

        if (foundUserDetails == null) {
            //model.addAttribute("passwordError", "User with this name is not found, please try again");
            bindingResult.addError(new ObjectError("username", "User with this name is not found, please try again"));
            return "login";
        }

        if (foundUserDetails.getPassword().equals(userForm.getPassword()) == false) {
            //model.addAttribute("passwordError", "Password is incorrect, please try again");
            bindingResult.addError(new ObjectError("passwordError", "Password is incorrect, please try again"));
            return "login";
        }

        return "redirect:/news";
    }*/

           /* if (bindingResult.hasErrors()) {
            return "login";
        }*/
}
