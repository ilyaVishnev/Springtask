package com.Entities;

import javax.persistence.*;

@Entity
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String url;
    private String shortUrl;
    private int redirectType;
    private int count = -1;
    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;

    public Url() {
    }

    public Url(String url, String shortUrl, int redirectType) {
        this.url = url;
        this.shortUrl = shortUrl;
        this.redirectType = redirectType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
