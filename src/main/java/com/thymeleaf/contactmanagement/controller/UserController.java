package com.thymeleaf.contactmanagement.controller;

import com.thymeleaf.contactmanagement.dao.UserRepository;
import com.thymeleaf.contactmanagement.entities.Contact;
import com.thymeleaf.contactmanagement.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //method for adding common data to response
    @ModelAttribute
    public void addCommonDate(Model model, Principal principal){

        String userName =  principal.getName();
        System.out.println("Username "+userName);
        User user = userRepository.getUserByUserName(userName);
        System.out.println("User "+user);
        model.addAttribute("user", user);

    }

    //dshboard home
    @GetMapping("/index")
    public String dashboard(Model model, Principal principal){

        model.addAttribute("title","User Dashboard");
        return "normal/user_dashboard";
    }

    // open add form handler

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }







}
