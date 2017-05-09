package com.amayadream.panspider.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author :  Amayadream
 * @date :  2017.05.08 22:04
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

    @RequestMapping(value = "")
    public String redirect() {
        return "redirect:/index";
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

}
