package com.example.userservice.service;

import com.example.userservice.entity.Account;

import java.util.List;

public interface IAccountService {

    public List<Account> getAllAccounts();
    Account createAccount(Account account);
    boolean deleteAccount(Long id);
    Boolean updateAccount(Account account, Long id);
    Account getAccountById(Long id);

}
