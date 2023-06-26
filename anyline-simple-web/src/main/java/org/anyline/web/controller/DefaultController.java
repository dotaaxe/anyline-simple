package org.anyline.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("web.home.DefaultController")
@RequestMapping("/")
public class DefaultController  {
    @RequestMapping("list")
    @ResponseBody
    public String list() {
         return "success";
    }
}
