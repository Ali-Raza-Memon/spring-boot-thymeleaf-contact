package com.thymeleaf.contactmanagement.entities;

import javax.persistence.*;

@Entity
@Table(name ="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean enabled;
    private String imageUrl;
    private String about;
}
