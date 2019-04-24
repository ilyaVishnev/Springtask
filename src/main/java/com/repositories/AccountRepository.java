package com.repositories;

import com.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account,Integer> {
    Account findByAccount(String account);
    List<Account> findAll();
}
