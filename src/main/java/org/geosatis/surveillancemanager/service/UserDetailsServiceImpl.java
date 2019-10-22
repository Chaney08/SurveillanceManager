package org.geosatis.surveillancemanager.service;


import org.geosatis.surveillancemanager.model.Role;
import org.geosatis.surveillancemanager.repository.RoleRepository;
import org.geosatis.surveillancemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


//UserDetailsService is a Spring security class that should be implemented to work in conjunction with web securty
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository appUserRepo;

    @Autowired
    private RoleRepository appRoleDAO;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        org.geosatis.surveillancemanager.model.User appUser = this.appUserRepo.findByUserName(userName);

        if (appUser == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        //We get our custom roles and use them to grant the default Spring user the related roles
        if (appUser.getRoles().size() > 0 ) {
            for (Role role : appUser.getRoles()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
                grantList.add(authority);
            }
        }

        UserDetails userDetails = (UserDetails) new User(appUser.getUserName(),
                appUser.getPassword(), grantList);

        return userDetails;
    }

}
