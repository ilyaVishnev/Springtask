package com.Controllers;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class SignInController {

    @RequestMapping("/sign")
    public ModelAndView gotoAccount(@RequestParam(required = false, value = "error") String error, @RequestParam(required = false, value = "logout") String logout, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if (error != null) {
            modelAndView.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
            modelAndView.setViewName("signIn");
        } else if (logout != null) {
            modelAndView.addObject("msg", "you've logout succesful");
            modelAndView.setViewName("hauptPage");
        } else {
            modelAndView.setViewName("signIn");
        }
        return modelAndView;
    }

    private String getErrorMessage(HttpServletRequest request, String key) {
        Exception exception = (Exception) request.getSession().getAttribute(key);
        String error = "";
        if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "invalid username and password";
        }
        return error;
    }
}
