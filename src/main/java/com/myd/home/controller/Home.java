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

        String webpage_title;
        String newsTitle;
        String newsUrl;
        HashMap<String, String> newsArticles = new HashMap<String, String>();


        model.addAttribute("username", principal.getName());

        try {

            //pulling information from wikipedia
            Document doc = Jsoup.connect("https://news.google.com/news/headlines/section/topic/TECHNOLOGY?ned=us&hl=en&gl=US").get();


            webpage_title = doc.title();

            //filters headlines by using a css query
            Elements newsHeadlines = doc.select("#mp-itn b a");

            for (Element headline : newsHeadlines) {

                //extracting the title and url from each headline article
                newsTitle = headline.attr("title");
                newsUrl = headline.absUrl("href");

                //adding it to the newsArticles Dictionary
                newsArticles.put(newsTitle, newsUrl);
            }
        } catch (IOException ex) {
            System.out.print(ex);
        }

        model.addAttribute("newsArticles", newsArticles);

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
