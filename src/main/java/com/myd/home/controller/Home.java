package com.myd.home.controller;

import com.myd.home.models.Links;
import com.myd.home.models.User;
import com.myd.home.models.data.UserDao;
import com.myd.home.models.GmailApi;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

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
    public String goHome(Principal principal, Model model) {

        ArrayList<Links> linkLists = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        String username = principal.getName();
        String exception;

        GmailApi emailList = new GmailApi();

        try {
            emails = emailList.generateEmails(username);
        } catch (MessagingException e) {
            exception = e.getMessage();
        } catch (IOException e) {
            exception = e.getMessage();
        }

        model.addAttribute("username", username);

        Links techNews = new Links("https://news.google.com/news/headlines/section/topic/TECHNOLOGY?ned=us&hl=en&gl=US", "[aria-level=2]");
        techNews.generateFilterdData();
        linkLists.add(techNews);

        Links topMovies = new Links("http://www.imdb.com/movies-in-theaters/", "h4 > a");
        topMovies.generateFilterdData();
        linkLists.add(topMovies);

        for (Links page : linkLists){

            int maxResults = 10;
            int index = 0;

            for (Element pageLink : page.getFilterdData()){

               if (index < maxResults){
                    page.setLinkTitle(pageLink.text());
                    page.setLinkUrl(pageLink.absUrl("href"));
                    page.addToLinkList(page.getLinkTitle(), page.getLinkUrl());
                    index++;
             } else {
                    break;
                    }


            }
        }

        model.addAttribute("linkLists", linkLists);
        model.addAttribute("emails", emails);

        return "homepage";

    }


    @PostMapping(value = "/dologin")
    public String loginPost(@ModelAttribute User user){


        return "homepage";

    }

    @GetMapping(value = "/signup")
    public String signupGet(Model model) {

        model.addAttribute("user", new User());

        return "signup";
    }

    @PostMapping(value = "/signup")
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
