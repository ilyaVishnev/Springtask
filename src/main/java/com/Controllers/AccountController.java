package com.controllers;

import com.entity.Account;
import com.entity.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.repositories.AccountRepository;
import com.repositories.UrlRepository;
import com.services.AccountService;
import com.services.UrlService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
public class AccountController {


    @Autowired
    public AccountService accountService;

    @Autowired
    public UrlService urlService;

    @PostMapping("/account")
    @ResponseBody
    public JSONObject getAccountAndPassword(@RequestBody String text) {
        HashMap<String, String> map = new Gson().fromJson(text, new TypeToken<HashMap<String, String>>() {
        }.getType());
        JSONObject send = new JSONObject();
        String account = map.get("AccountId");
        Account myAccount = accountService.findByAccount(account);
        if (myAccount == null) {
            send.put("success", true);
            send.put("description", "you've succesfull created account");
            String password = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            send.put("password", password);
            accountService.save(new Account(account, password));
        } else {
            send.put("success", false);
            send.put("description", "account with that ID already exists");
        }
        return send;
    }

    @GetMapping("/**")
    public ModelAndView getAllrequests(HttpServletRequest request) {
        String url = request.getServletPath().substring(1);
        Url myUrl = urlService.findByUrl(url);
        ModelAndView modelAndView;
        if (myUrl != null) {
            String longUrl = myUrl.getUrl();
            myUrl.setCount(myUrl.getCount() + 1);
            urlService.save(myUrl);
            RedirectView redirectView = new RedirectView("/shorturl", true);
            if (myUrl.getRedirectType() == 301) {
                redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            }
            modelAndView = new ModelAndView(redirectView);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setViewName("noHtml");
        }
        return modelAndView;
    }

    @GetMapping("/shorturl")
    public String getShortUrlPage() {
        return "shortUrl";
    }

    @GetMapping("/help")
    public String getHelp() {
        return "help";
    }
}
