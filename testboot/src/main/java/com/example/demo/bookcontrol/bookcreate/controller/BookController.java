package com.example.demo.bookcontrol.bookcreate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.bookcontrol.bookcreate.entity.BookDto;
import com.example.demo.bookcontrol.bookcreate.service.BookService;

@Controller
public class BookController {

	@Autowired
	private BookService bookService; //[cite: 1]

	@GetMapping("/books/register") //[cite: 1]
	public String showRegisterForm(Model model) { //[cite: 1]
		model.addAttribute("bookDto", new BookDto()); //[cite: 1]
		return "bookmanage/bookcreate/register"; // ★ここがフォルダ階層と合っているかチェック！
	}

	/**
     * 【変更点】ISBN検索API (画面リロードせずJSONデータを返却)
     */
    @PostMapping("/books/search")
    @ResponseBody
    public ResponseEntity<BookDto> searchBookByIsbn(@RequestParam("isbn") String isbn) {
        BookDto bookDto = bookService.fetchBookInfoFromNdl(isbn);
        return ResponseEntity.ok(bookDto);
    }

	@PostMapping("/books/register") //[cite: 1]
	public String registerBook(@ModelAttribute BookDto bookDto, Model model) { //[cite: 1]
		bookService.saveBook(bookDto); //[cite: 1]
		model.addAttribute("successMessage", "「" + bookDto.getTitle() + "」を登録しました！"); //[cite: 1]
		model.addAttribute("bookDto", new BookDto()); //[cite: 1]
		return "bookmanage/bookcreate/register"; // ★
	}

	/**
	 * JavaScriptから送信された複数件の書籍情報を一括登録するAPI
	 */
	@PostMapping("/books/register-batch")
	@ResponseBody
	public ResponseEntity<String> registerBooksBatch(@RequestBody List<BookDto> dtoList) {
		if (dtoList == null || dtoList.isEmpty()) {
			return ResponseEntity.badRequest().body("登録対象のデータがありません。");
		}
		bookService.saveAllBooks(dtoList);
		return ResponseEntity.ok(dtoList.size() + "件の書籍を一括登録しました！");
	}
}
