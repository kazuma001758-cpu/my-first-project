package com.example.demo.bookcontrol.bookdelete.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.bookcontrol.bookdelete.service.BookDeleteService;

@Controller
public class BookDeleteController {

    @Autowired
    private BookDeleteService bookDeleteService;

    @GetMapping("/books/delete")
    public String showDeletePage() {
        return "bookmanage/bookdelete/delete";
    }

    /**
     * 一括論理削除API (POST /books/delete/bulk)
     */
    @PostMapping("/books/delete/bulk")
    @ResponseBody
    public ResponseEntity<String> bulkDeleteBooks(@RequestBody List<String> isbns) {
        if (isbns == null || isbns.isEmpty()) {
            return ResponseEntity.badRequest().body("削除対象が選択されていません。");
        }

        int deletedCount = bookDeleteService.softDeleteBooks(isbns);
        return ResponseEntity.ok(deletedCount + " 件の書籍を削除（非表示設定）しました。");
    }
}