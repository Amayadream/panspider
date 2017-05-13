package com.amayadream.panspider.web.controller;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 *
 * @author :  Amayadream
 * @date :  2017.05.08 22:04
 */
@Controller
@RequestMapping(value = "")
public class IndexController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "")
    public String redirect() {
        return "redirect:/index";
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    /**
     * 搜索
     * @param q 关键词
     */
    @RequestMapping(value = "/search")
    public String search(@RequestParam String q, Model model) {
        model.addAttribute("q", q);
        return "/result";
    }

    /**
     * 详情页
     * @param shareId 分享编号
     */
    @RequestMapping(value = "/share/{shareId}")
    public String result(@PathVariable String shareId) {

        return null;
    }

    /**
     * 关于
     */
    @RequestMapping(value = "/about")
    public String about() {
        return "/about";
    }


}
