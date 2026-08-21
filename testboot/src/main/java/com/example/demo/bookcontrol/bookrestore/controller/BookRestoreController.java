package com.example.demo.bookcontrol.bookrestore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.bookcontrol.bookrestore.service.BookRestoreService;

@Controller
public class BookRestoreController {

    @Autowired
    private BookRestoreService bookRestoreService;

    @GetMapping("/books/restore")
    public String showRestorePage() {
        return "bookmanage/bookrestore/restore";
    }

    /**
     * 一括論理復元API (POST /books/restore/bulk)
     */
    @PostMapping("/books/restore/bulk")
    @ResponseBody
    public ResponseEntity<String> bulkRestoreBooks(@RequestBody List<String> isbns) {
        if (isbns == null || isbns.isEmpty()) {
            return ResponseEntity.badRequest().body("復元対象が選択されていません。");
        }

        int restoredCount = bookRestoreService.restoreBooks(isbns);
        return ResponseEntity.ok(restoredCount + " 件の書籍を復元（再表示設定）しました。");
    }
}