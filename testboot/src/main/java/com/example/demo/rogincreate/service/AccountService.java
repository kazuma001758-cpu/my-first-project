package com.example.demo.rogincreate.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.rogincreate.model.Account;
import com.example.demo.rogincreate.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // アカウント作成
    public boolean create(String username, String rawPassword) {
        // すでに存在している場合は作成不可
        if (accountRepository.existsById(username)) {
            return false;
        }

        Account account = new Account();
        account.setUsername(username);
        // パスワードを暗号化してセット
        account.setPassword(passwordEncoder.encode(rawPassword));

        accountRepository.save(account);
        return true;
    }

    // ログイン認証
    public boolean authenticate(String username, String rawPassword) {
        Optional<Account> optionalAccount = accountRepository.findById(username);
        if (optionalAccount.isEmpty()) {
            return false; // usernameが存在しない
        }

        Account account = optionalAccount.get();
        // 入力されたパスワードとDBの暗号化パスワードを照合
        return passwordEncoder.matches(rawPassword, account.getPassword());
    }
}