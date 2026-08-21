package com.example.demo.bookcontrol.bookcreate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bookcontrol.bookcreate.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    // String は主キー（ISBN）の型です
}