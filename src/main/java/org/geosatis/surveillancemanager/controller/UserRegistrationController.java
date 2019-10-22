package org.geosatis.surveillancemanager.controller;

import org.geosatis.surveillancemanager.model.Role;
import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.RoleRepository;
import org.geosatis.surveillancemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("user")
    public User userRegistrationDto() {
        return new User();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("userRegistration", user);
        return "userTemplates/registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("userRegistration") @Valid User user,
                                      BindingResult result,@RequestParam(value ="admin", defaultValue="false") Boolean admin) {

        User existing = userRepo.findByUserName(user.getUserName());
        if (existing != null) {
            result.rejectValue("userName", null, "There is already an account registered with that username");
        }

        if (result.hasErrors()) {
            return "userTemplates/registration";
        }else {
            List<Role> userPermission;
            if(admin){
                userPermission = roleRepo.getRoleByRoleName("ROLE_ADMIN");
            }else{
                userPermission = roleRepo.getRoleByRoleName("ROLE_USER");
            }

            user.setRoles(userPermission);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
            return "redirect:/registration?success";
        }
    }
}