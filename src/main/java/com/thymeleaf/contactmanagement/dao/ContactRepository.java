package com.thymeleaf.contactmanagement.dao;

import com.thymeleaf.contactmanagement.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer>{
    //pagination...
    @Query("SELECT contact FROM Contact contact WHERE contact.user.id=?1")
    List<Contact> findContactsByUser(int userId);

}
