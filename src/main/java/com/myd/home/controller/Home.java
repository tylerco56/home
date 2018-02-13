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
        HashMap<String, String> techArticles = new HashMap<String, String>();
        HashMap<String, String> topArticles = new HashMap<String, String>();


        model.addAttribute("username", principal.getName());

        try {

            //pulling information from google news
            Document techDoc = Jsoup.connect("https://news.google.com/news/headlines/section/topic/TECHNOLOGY?ned=us&hl=en&gl=US").get();
            Document topDoc = Jsoup.connect("https://news.google.com/news/headlines?ned=us&hl=en&gl=US").get();

            webpage_title = techDoc.title();

            //filters headlines by using a css query
            Elements techHeadlines = techDoc.select("[aria-level=2]");
            Elements topHeadlines = topDoc.select("[aria-level=2]");

            for (Element headline : techHeadlines) {

                //extracting the title and url from each headline article
                newsTitle = headline.text();
                newsUrl = headline.absUrl("href");

                //adding it to the newsArticles Dictionary
                techArticles.put(newsTitle, newsUrl);
            }

            for (Element headline : topHeadlines) {

                //extracting the title and url from each headline article
                newsTitle = headline.text();
                newsUrl = headline.absUrl("href");

                //adding it to the newsArticles Dictionary
                topArticles.put(newsTitle, newsUrl);
            }

        } catch (IOException ex) {
            System.out.print(ex);
        }

        model.addAttribute("techArticles", techArticles);
        model.addAttribute("topArticles", topArticles);

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
