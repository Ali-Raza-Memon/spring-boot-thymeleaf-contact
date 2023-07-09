package com.thymeleaf.contactmanagement.controller;

import com.thymeleaf.contactmanagement.dao.UserRepository;
import com.thymeleaf.contactmanagement.entities.Contact;
import com.thymeleaf.contactmanagement.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        User user = new User();
        user.setName("Durgesh Tiwari");
        user.setEmail("durgesh@gmail.com");
        Contact contact = new Contact();
        user.getContacts().add(contact);
        userRepository.save(user);
        return "Working";
    }

}
