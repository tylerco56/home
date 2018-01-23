package com.myd.home.controller;

import com.myd.home.models.User;
import com.myd.home.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Created by: Tyler Langenfeld
 */

@Controller
public class Home {

    @Autowired
    private UserDao userDao;

    @GetMapping(value = "/login")
    public String loginGet(Model model) {

        model.addAttribute("title", "Login");
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping(value = "/login")
    public String loginPost(@ModelAttribute User user, Model model){

        User realUser = userDao.findByUserName(user.getUserName());

        if (realUser.getId() != null) {
            return "homepage";
        } else {
            model.addAttribute("error", "Invalid Username or Password");
            model.addAttribute("user", new User());
            return "login";
        }

    }

    @GetMapping(value = "/signup")
    public String signupGet(Model model) {

        model.addAttribute("user", new User());

        return "signup";
    }

    @PostMapping(value = "signup")
    public String userSignupPost(@ModelAttribute @Valid User user, Errors errors, Model model){

        Boolean passMismatch = false;

        /** Check if passwords match! **/
        if (!user.getPassword().equals(user.getPasswordVerify())) {
            passMismatch = true;
            model.addAttribute("passMismatch", "Passwords do not match!");
        } else {
            passMismatch = false;
            model.addAttribute("passMismatch", "");
        }

        if(errors.hasErrors() || passMismatch ){
            return "signup";
        }

        userDao.save(user);
        return "login";
    }
}
