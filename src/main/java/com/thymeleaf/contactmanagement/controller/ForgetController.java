package com.thymeleaf.contactmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ForgetController {
    int otp = ThreadLocalRandom.current().nextInt(1000, 100000);

    @GetMapping("/forgot")
    public String openEmailForm(){

        return "forgot_email_form";
    }




    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email")String email){

        System.out.println("Email "+email);
        //generating otp of 4 digit



        System.out.println("OTP : "+otp);
        return "normal/verify_otp";

    }



}
