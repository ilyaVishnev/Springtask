package com.Controllers;

import com.Entities.Account;
import com.Entities.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.repositories.AccountRepository;
import com.repositories.UrlRepository;
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
    public AccountRepository accountRepository;

    @Autowired
    public UrlRepository urlRepository;

    @GetMapping("/haupt")
    public ModelAndView hauptPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hauptPage");
        return modelAndView;
    }

    @PostMapping("/account")
    @ResponseBody
    public JSONObject getAccountAndPassword(@RequestBody String text) {
        HashMap<String, String> map = new Gson().fromJson(text, new TypeToken<HashMap<String, String>>() {
        }.getType());
        JSONObject send = new JSONObject();
        String account = map.get("AccountId");
        Account myAccount = accountRepository.findByAccount(account);
        if (myAccount == null) {
            send.put("success", true);
            send.put("description", "you've succesfull created account");
            String password = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            send.put("password", password);
            accountRepository.save(new Account(account, password));
        } else {
            send.put("success", false);
            send.put("description", "account with that ID already exists");
        }
        return send;
    }

    @PostMapping("/register")
    @ResponseBody
    @Secured("ROLE_USER")
    public JSONObject getShortUrl(@RequestBody String text) {
        HashMap<String, String> map = new Gson().fromJson(text, new TypeToken<HashMap<String, String>>() {
        }.getType());
        String url = map.get("url");
        String redirectType = map.get("redirectType");
        JSONObject send = new JSONObject();
        String shortUrl = url.substring(0, url.toCharArray().length - url.toCharArray().length / 3);
        send.put("shortUrl", shortUrl);
        redirectType = redirectType != null && redirectType.equals("302") ? "302" : "301";
        urlRepository.save(new Url(url, shortUrl, Integer.parseInt(redirectType)));
        final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByAccount(currentUserName);
        Url myUrl = urlRepository.findByUrl(url);
        account.getUrls().add(myUrl);
        myUrl.setAccount(account);
        accountRepository.save(account);
        urlRepository.save(myUrl);
        return send;
    }

    @GetMapping("/statistic/{AccountId}")
    @ResponseBody
    @Secured("ROLE_USER")
    public JSONObject getStatistic(@PathVariable("AccountId") String accountId) {
        JSONObject send = new JSONObject();
        Account account = accountRepository.findByAccount(accountId);
        Iterator<Url> iterator = account.getUrls().iterator();
        while (iterator.hasNext()) {
            Url url = iterator.next();
            send.put(url.getUrl(), ": " + url.getCount());
        }
        return send;
    }

    @GetMapping("/**")
    public ModelAndView getAllrequests(HttpServletRequest request) {
        String url = request.getServletPath().substring(1);
        Url myUrl = urlRepository.findByUrl(url);
        ModelAndView modelAndView;
        if (myUrl != null) {
            String longUrl = myUrl.getUrl();
            myUrl.setCount(myUrl.getCount() + 1);
            urlRepository.save(myUrl);
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
