package com.repositories;

import com.Entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account,Integer> {
    Account findByAccount(String account);
}
