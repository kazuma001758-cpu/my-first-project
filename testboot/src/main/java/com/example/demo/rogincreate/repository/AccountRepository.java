package com.example.demo.rogincreate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.rogincreate.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    // 主キー（username）による検索は JpaRepository の findById(username) が標準で利用できます
}