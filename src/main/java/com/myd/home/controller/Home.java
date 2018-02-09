package com.myd.home.controller;

import ch.qos.logback.classic.spi.LoggerRemoteView;
import com.myd.home.models.User;
import com.myd.home.models.data.UserDao;
import com.sun.org.apache.xpath.internal.operations.Mod;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;




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


    @RequestMapping(value="/homepage", method = RequestMethod.GET)
    public String goHome(Principal principal, Model model){

        String scraped_Data = "";

        model.addAttribute("username", principal.getName());

        try {
            Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
            scraped_Data += doc.title() + "<br />";
            Elements newsHeadlines = doc.select("#mp-itn b a");
            for (Element headline : newsHeadlines) {
                scraped_Data += headline.attr("title") + headline.absUrl("href") + "<br /><br />";
            }

        } catch (IOException ex) {
            System.out.print(ex);
        }

        model.addAttribute("headLines", scraped_Data );

        return "homepage";
    }

    /**
    @PostMapping(value = "/dologin")
    public String loginPost(@ModelAttribute User user, Model model){

        return "homepage";

    }**/

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
