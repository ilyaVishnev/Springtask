package com.controllers;

import com.entity.Account;
import com.entity.Url;
import com.repositories.AccountRepository;
import com.services.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

@Controller
public class StatisticController {

    @Autowired
    public AccountService accountService;

    @GetMapping("/statistic/{AccountId}")
    @ResponseBody
    @Secured("ROLE_USER")
    public JSONObject getStatistic(@PathVariable("AccountId") String accountId) {
        JSONObject send = new JSONObject();
        Account account = accountService.findByAccount(accountId);
        Iterator<Url> iterator = account.getUrls().iterator();
        while (iterator.hasNext()) {
            Url url = iterator.next();
            send.put(url.getUrl(), url.getCount());
        }
        return send;
    }
}
