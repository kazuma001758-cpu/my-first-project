package com.example.demo.SW.SWcreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateMenuController {

    @GetMapping("/SW/create/createmenu")
    public String showCreateMenu() {
        // templates/SW/create/createmenu.html を返す
        return "SW/create/createmenu";
    }
}