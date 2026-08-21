package com.example.demo.bookcontrol.bookdelete.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bookcontrol.bookcreate.repository.BookRepository;

@Service
public class BookDeleteService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * 複数件の一括論理削除処理
     */
    @Transactional
    public int softDeleteBooks(List<String> isbns) {
        int count = 0;
        for (String isbn : isbns) {
            boolean isUpdated = bookRepository.findById(isbn).map(book -> {
                book.setDeleted(true);
                book.setDeletedAt(LocalDateTime.now());
                bookRepository.save(book);
                return true;
            }).orElse(false);

            if (isUpdated) {
                count++;
            }
        }
        return count; // 削除できた件数を返す
    }
}