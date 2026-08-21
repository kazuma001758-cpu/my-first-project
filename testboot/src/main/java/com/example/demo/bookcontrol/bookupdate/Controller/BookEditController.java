
package com.example.demo.bookcontrol.bookupdate.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.bookcontrol.bookcreate.entity.BookDto;
import com.example.demo.bookcontrol.bookcreate.service.BookService;

@Controller
public class BookEditController {

    @Autowired
    private BookService bookUpdateService;

    /**
     * 1. 編集画面表示 (GET /books/edit)
     */
    @GetMapping("/books/edit")
    public String showEditForm(@RequestParam(value = "isbn", required = false) String isbn, Model model) {
        BookDto bookDto = null;
        if (isbn != null && !isbn.isEmpty()) {
            bookDto = bookUpdateService.findBookByIsbn(isbn);
        }
        if (bookDto == null) {
            bookDto = new BookDto();
        }
        model.addAttribute("bookDto", bookDto);
        return "bookmanage/bookupdate/update";
    }

    /**
     * 2. ISBN検索用API (GET /books/api/search?isbn=xxx) ★新規追加
     */
    @GetMapping("/books/api/search")
    @ResponseBody
    public ResponseEntity<?> searchByIsbn(@RequestParam("isbn") String isbn) {
        BookDto bookDto = bookUpdateService.findBookByIsbn(isbn);
        if (bookDto == null) {
            return ResponseEntity.status(404).body("該当する書籍が見つかりませんでした。");
        }
        return ResponseEntity.ok(bookDto);
    }

    /**
     * 3. 更新処理API (POST /books/update)
     */
    @PostMapping("/books/update")
    @ResponseBody
    public ResponseEntity<String> updateBook(@RequestBody BookDto bookDto) {
        if (bookDto.getIsbn() == null || bookDto.getIsbn().isEmpty()) {
            return ResponseEntity.badRequest().body("ISBNが不正です。");
        }
        bookUpdateService.saveBook(bookDto);
        return ResponseEntity.ok("「" + bookDto.getTitle() + "」の情報を更新しました！");
    }
}