package com.example.demo.bookcontrol.bookrestore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bookcontrol.bookcreate.repository.BookRepository;

@Service
public class BookRestoreService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * 複数件の一括論理復元処理（deletedフラグをfalseにし、削除日時をnullに戻す）
     */
    @Transactional
    public int restoreBooks(List<String> isbns) {
        int count = 0;
        for (String isbn : isbns) {
            boolean isUpdated = bookRepository.findById(isbn).map(book -> {
                book.setDeleted(false);
                book.setDeletedAt(null);
                bookRepository.save(book);
                return true;
            }).orElse(false);

            if (isUpdated) {
                count++;
            }
        }
        return count; // 復元できた件数を返す
    }
}