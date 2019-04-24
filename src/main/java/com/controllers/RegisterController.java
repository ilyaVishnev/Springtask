package com.controllers;

import com.entity.Account;
import com.entity.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.repositories.AccountRepository;
import com.repositories.UrlRepository;
import com.services.AccountService;
import com.services.UrlService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class RegisterController {

    @Autowired
    public AccountService accountService;

    @Autowired
    public UrlService urlService;

    @PostMapping("/register")
    @ResponseBody
    @Secured("ROLE_USER")
    public JSONObject getShortUrl(@RequestBody String text) {
        HashMap<String, String> map = new Gson().fromJson(text, new TypeToken<HashMap<String, String>>() {
        }.getType());
        JSONObject send = new JSONObject();
        String url = map.get("url");
        Url myUrl = urlService.findByUrl(url);
        if (myUrl == null) {
            String redirectType = map.get("redirectType");
            String shortUrl = url.substring(0, url.toCharArray().length - url.toCharArray().length / 3);
            send.put("shortUrl", shortUrl);
            redirectType = redirectType != null && redirectType.equals("302") ? "302" : "301";
            urlService.save(new Url(url, shortUrl, Integer.parseInt(redirectType)));
            final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountService.findByAccount(currentUserName);
            myUrl = urlService.findByUrl(url);
            account.getUrls().add(myUrl);
            myUrl.setAccount(account);
            accountService.save(account);
            urlService.save(myUrl);
            send.put("success", true);
        } else {
            send.put("success", false);
            send.put("description", "url already exists");
        }
        return send;
    }
}
