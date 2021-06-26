package com.cloudphotosapp.cloudphotosapp.controller;

import com.cloudphotosapp.cloudphotosapp.domain.User;
import com.cloudphotosapp.cloudphotosapp.repository.RoleRepository;
import com.cloudphotosapp.cloudphotosapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin/showAll")
    String getUsers(Model model){

        List<User> users = userService.findAll();

        model.addAttribute("users", users);

        return "admin/showAll";
    }
}
