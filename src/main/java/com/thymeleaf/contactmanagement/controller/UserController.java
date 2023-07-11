package com.thymeleaf.contactmanagement.controller;

import com.thymeleaf.contactmanagement.dao.ContactRepository;
import com.thymeleaf.contactmanagement.dao.UserRepository;
import com.thymeleaf.contactmanagement.entities.Contact;
import com.thymeleaf.contactmanagement.entities.User;
import com.thymeleaf.contactmanagement.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    //method for adding common data to response
    @ModelAttribute
    public void addCommonDate(Model model, Principal principal){

        String userName =  principal.getName();
        System.out.println("Username "+userName);
        User user = userRepository.getUserByUserName(userName);
        System.out.println("User "+user);
        model.addAttribute("user", user);

    }

    //dashboard home
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


//    processing add contact
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
                                 @RequestParam("profileImage")MultipartFile file
                                 , Principal principal,HttpSession session){

        try{
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            //processing and uploading file...

            if(file.isEmpty())
                System.out.println("File is empty");
            else{
                //uploading file to folder and update the name to contact
                contact.setImage(file.getOriginalFilename());

                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

                Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image is uploaded");
            }

            contact.setUser(user);
            user.getContacts().add(contact);

            this.userRepository.save(user);


            System.out.println("Data "+contact);
            System.out.println("Added to data base");

            //message success
            session.setAttribute("message",new Message("Your content is added ! add more", "success"));

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error "+e.getMessage());
            //message error
            session.setAttribute("message", new Message("Something went wrong. Try again","danger"));
        }

        return "normal/add_contact_form";

    }

    //show contacts handler
    @GetMapping("/show_contacts")
    public String showContacts(Model model,Principal principal){

        model.addAttribute("title","Show User Contacts");

        //contact ki list ko send karna hai

//        String userName = principal.getName();
//        User user = this.userRepository.getUserByUserName(userName);
//        List<Contact> contacts =   user.getContacts();
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        List<Contact> contacts = this.contactRepository.findContactsByUser(user.getId());

        model.addAttribute("contacts",contacts);

        return "normal/show_contacts";
    }



}
