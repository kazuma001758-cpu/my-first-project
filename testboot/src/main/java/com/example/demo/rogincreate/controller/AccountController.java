package com.example.demo.rogincreate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.rogincreate.service.AccountService;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    // ログイン画面表示
    @GetMapping("/login")
    public String showLoginForm() {
        return "account/login"; // ← フォルダ名を追加
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {

        if (accountService.authenticate(username, password)) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "ユーザー名またはパスワードが間違っています。");
            return "account/login"; // ← フォルダ名を追加
        }
    }

    // アカウント作成画面表示
    @GetMapping("/create")
    public String showRegisterForm() {
        return "account/create"; // ← フォルダ名を追加
    }

    // アカウント作成処理
    @PostMapping("/create")
    public String create(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {

        if (accountService.create(username, password)) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "そのユーザー名は既に使われています。");
            return "account/create"; // ← フォルダ名を追加
        }
    }
}