package org.geosatis.surveillancemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping(path="/")
public class MainController {

    @RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "userTemplates/loginPage";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        return "userTemplates/loginPage";
    }
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
        return "403Page";
    }
}