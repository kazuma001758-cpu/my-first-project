package com.example.demo.bookcontrol;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookManageController {

    @GetMapping("/bookmanage")
    public String showBookManagePage() {
        // templates/bookmanage/bookmanage.html を指定
        return "bookmanage/bookmanage";
    }
}