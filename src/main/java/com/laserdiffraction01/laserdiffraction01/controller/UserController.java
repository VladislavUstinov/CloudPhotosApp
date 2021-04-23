package com.laserdiffraction01.laserdiffraction01.controller;

import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/showAll")
    String getUsers(Model model){

        List<User> users = userService.findAll();

        model.addAttribute("users", users);

        return "/user/showAll";
    }
}
