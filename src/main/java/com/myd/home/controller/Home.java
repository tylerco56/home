package com.myd.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by: Tyler Langenfeld
 */

@Controller
public class Home {

    @RequestMapping(value = "")
    public String index() {

        return "signup";
    }
}
