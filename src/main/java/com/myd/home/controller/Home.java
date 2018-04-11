package com.myd.home.controller;
import com.myd.home.models.GmailApi;
import com.myd.home.models.Link;
import com.myd.home.models.JSoupApi;
import com.myd.home.models.User;
import com.myd.home.models.data.LinkDao;
import com.myd.home.models.data.UserDao;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;



/**
 * Created by: Tyler Langenfeld
 */
@Controller
public class Home {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LinkDao linkDao;

    private String code = null;

    GmailApi service = new GmailApi();

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(Model model) {

        model.addAttribute("title", "Login");

        model.addAttribute("code", null);

        return "login";
    }

    @RequestMapping(value="/homepage", method = { RequestMethod.GET, RequestMethod.POST } )
    public String goHome(Principal principal, Model model, @RequestParam(required = false) String code,
                         @RequestParam(defaultValue = "0") Integer serviceId) {

        ArrayList<Link> sourceSiteLink = new ArrayList<>();
        ArrayList<JSoupApi> subjectSoup = new ArrayList<>();
        ArrayList<JSoupApi> subjects = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        User userLoggedIn = userDao.findByName(principal.getName());

        String exception;

        if (code != null) {
            try {
                emails = service.generateEmails(code);
            } catch (MessagingException e) {
                exception = e.getMessage();
            } catch (IOException e) {
                exception = e.getMessage();
            }
        }

        if (serviceId > 0) {
            Link newService = linkDao.findOne(serviceId);
            userLoggedIn.addLink(newService);
            userDao.save(userLoggedIn);
        }

        model.addAttribute("subjectMenu", linkDao.findAll());
        model.addAttribute("username", principal.getName());

        Link googleNews = new Link("https://news.google.com/news/?ned=us&gl=US&hl=en", "div.JAPqpe > a[target='_self']");
        sourceSiteLink.add(googleNews);

        //com.myd.home.models.Link yahooNews = new Link("https://www.yahoo.com/news/", "ul > li > a[title]");
        //sourceSiteLink.add(yahooNews);


        //pull major subject links from main website
        for (Link link : sourceSiteLink) {
            JSoupApi pageLinks = new JSoupApi(link.getUrl(), link.getFilter());
            pageLinks.generateFilteredData();
            //source site link list contains the generated objects from jsoup api
            subjectSoup.add(pageLinks);
        }

        //loops through each links object
        for (JSoupApi subject : subjectSoup) {
            //then cycles through the generated data by applying the filter
            //result is individual elements
            for (Element category : subject.getFilterdData()) {
                //if subject link isn't in the database then it will add it there
                if (linkDao.findLinkByUrl(category.absUrl("href").toString()) == null) {

                    //set news subject news title
                    subject.setLinkTitle(category.text());
                    subject.setLinkUrl(category.absUrl("href"));
                    String filter = "";

                    if (subject.getLinkUrl().contains("google")) {
                        filter = "[aria-level=2]";
                        subject.setLinkTitle("Google - " + category.text().substring(1));
                    } else {
                        filter = "a[class*='Fw(b) Fz(20px) Lh(23px)']";
                    }


                    Link categoryLink = new Link(subject.getLinkTitle(), subject.getLinkUrl(), filter);

                    linkDao.save(categoryLink);
                }

            }

            //Add single site link
            if (linkDao.findLinkByName("Movies In Theaters") == null) {
                Link categoryLink = new Link("Movies In Theaters", "http://www.imdb.com/movies-in-theaters/", "h4 > a");
                linkDao.save(categoryLink);
            }

        }


        for (Link subjectLinks : userLoggedIn.getServices()){

        JSoupApi loadSubjectSoup = new JSoupApi(subjectLinks.getUrl(), subjectLinks.getFilter());
        loadSubjectSoup.generateFilteredData();
        subjects.add(loadSubjectSoup);

        }

        for (JSoupApi page : subjects){

            int maxResults = 10;
            int index = 0;

            for (Element headline : page.getFilterdData()){

               if (index < maxResults){
                    page.setLinkTitle(headline.text());
                    page.setLinkUrl(headline.absUrl("href"));
                    page.addToLinkList(page.getLinkTitle(), page.getLinkUrl());
                    index++;
             } else {
                    break;
                    }

            }
        }


        model.addAttribute("linkLists", subjects);
        model.addAttribute("emails", emails);
        model.addAttribute("code", code);

        return "homepage";

    }

    @PostMapping(value = "/dologin")
    public String loginPost(){


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

    @RequestMapping(value = "/gmail", method = RequestMethod.GET)
    public ModelAndView googleConnectionStatus() throws Exception {

        String url = service.authorize();

        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {

        return "logout";
    }
 }