package com.repositories;

import com.Entities.Url;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UrlRepository extends CrudRepository<Url, Integer> {
    Url findByUrlOrShortUrl(String url,String shortUrl);
    Url findByUrl(String url);
}
