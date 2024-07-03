package com.example.userservice.service.impl;

import com.example.userservice.entity.Account;
import com.example.userservice.repository.AccountRepository;
import com.example.userservice.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implAccountService implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public boolean deleteAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            accountRepository.delete(optionalAccount.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateAccount(Account account, Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account existingAccount = optionalAccount.get();
            existingAccount.setPassword(account.getPassword());
            existingAccount.setStatus(account.getStatus());
            existingAccount.setRole(account.getRole());
            accountRepository.save(existingAccount);
            return true;
        }
        return false;
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }
}
