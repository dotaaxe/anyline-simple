package org.anyline.simple.web.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("web.home.DefaultController")
@RequestMapping("/")
public class DefaultController extends BasicController {
    @RequestMapping({"","index"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = template("index.jsp");

        return mv;
    }

}
