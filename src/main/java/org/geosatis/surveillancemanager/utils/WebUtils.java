package org.geosatis.surveillancemanager.utils;

import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Configurable
//A small utils class to make getting user easier
public class WebUtils {

    @Autowired
    private UserRepository appUserRepo;

    public User getUser(){
        return this.appUserRepo.findByUserName(getUserName());
    }

    public String getUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}