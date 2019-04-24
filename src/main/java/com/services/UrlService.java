package com.services;

import com.entity.Url;
import com.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public Url findByUrlOrShortUrl(String url, String shortUrl) {
        return urlRepository.findByUrlOrShortUrl(url, shortUrl);
    }

    public Url findByUrl(String url) {
        return urlRepository.findByUrl(url);
    }

    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    public void save(Url url) {
        urlRepository.save(url);
    }
}
