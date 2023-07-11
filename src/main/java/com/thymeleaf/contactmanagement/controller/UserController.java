package com.thymeleaf.contactmanagement.controller;

import com.thymeleaf.contactmanagement.dao.UserRepository;
import com.thymeleaf.contactmanagement.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal){

        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user",user);
        return "normal/user_dashboard";
    }

}
