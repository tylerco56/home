package com.myd.home.controller;

import ch.qos.logback.classic.spi.LoggerRemoteView;
import com.myd.home.models.Links;
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
import java.util.ArrayList;
import java.util.HashMap;

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

        ArrayList<Links> linkLists = new ArrayList<>();

        model.addAttribute("username", principal.getName());

        Links techNews = new Links("https://news.google.com/news/headlines/section/topic/TECHNOLOGY?ned=us&hl=en&gl=US", "[aria-level=2]");
        techNews.generateFilterdData();
        linkLists.add(techNews);

        Links topMovies = new Links("http://www.imdb.com/movies-in-theaters/", "h4 > a");
        topMovies.generateFilterdData();
        linkLists.add(topMovies);

        for (Links page : linkLists){

            for (Element pageLink : page.getFilterdData()){

                page.setLinkTitle(pageLink.text());
                page.setLinkUrl(pageLink.absUrl("href"));

                page.addToLinkList(page.getLinkTitle(), page.getLinkUrl());
            }
        }

        model.addAttribute("linkLists", linkLists);

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
