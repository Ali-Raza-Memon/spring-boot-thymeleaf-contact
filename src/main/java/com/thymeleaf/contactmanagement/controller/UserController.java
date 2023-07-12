package com.thymeleaf.contactmanagement.controller;

import com.thymeleaf.contactmanagement.dao.ContactRepository;
import com.thymeleaf.contactmanagement.dao.UserRepository;
import com.thymeleaf.contactmanagement.entities.Contact;
import com.thymeleaf.contactmanagement.entities.User;
import com.thymeleaf.contactmanagement.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.awt.print.Printable;
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

            if(file.isEmpty()) {
                System.out.println("File is empty");
                contact.setImage("contact.png");
            }
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
    //example (per page =5)
    //current page =0 [page]
    @GetMapping("/show_contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page,Model model,Principal principal){

        model.addAttribute("title","Show User Contacts");

        //contact ki list ko send karna hai

//        String userName = principal.getName();
//        User user = this.userRepository.getUserByUserName(userName);
//        List<Contact> contacts =   user.getContacts();
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        Pageable pageable = PageRequest.of(page,5);

        Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable);

        model.addAttribute("contacts",contacts);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",contacts.getTotalPages());

        return "normal/show_contacts";
    }

    //showing particular contact details.

    @GetMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal){

        System.out.println("CID"+cId);

        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);
        Contact contact = this.contactRepository.findById(cId).get();

        if(user.getId()==contact.getUser().getId()) {
            model.addAttribute("contact", contact);
            model.addAttribute("title",contact.getName());
        }

        return "normal/contact_detail";
    }


    //delete contact handler

    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cId,Model model, HttpSession session){

       Contact contact = this.contactRepository.findById(cId).get();
//
       contact.setUser(null);
        //Check.. Assignment

        //remove image
        //content.getImage();

       this.contactRepository.delete(contact);

       //contact deleted successfully

        // then image should also be deleted


        session.setAttribute("message", new Message("Contact deleted successfully", "success"));

       return "redirect:/user/show_contacts/0";
    }




}
