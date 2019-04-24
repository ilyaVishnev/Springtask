package com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HauptController {

    @GetMapping("/haupt")
    public ModelAndView hauptPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hauptPage");
        return modelAndView;
    }
}
