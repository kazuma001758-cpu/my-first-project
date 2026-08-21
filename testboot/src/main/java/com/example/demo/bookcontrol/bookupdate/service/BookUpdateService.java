package com.example.demo.bookcontrol.bookupdate.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bookcontrol.bookcreate.entity.Book;
import com.example.demo.bookcontrol.bookcreate.entity.BookDto;
import com.example.demo.bookcontrol.bookcreate.repository.BookRepository;

@Service
public class BookUpdateService {

    @Autowired
    private BookRepository bookRepository;

    public BookDto fetchBookInfoFromNdl(String isbn) {
        return new BookDto(); 
    }

    public void saveBook(BookDto dto) {
        Book entity = convertToEntity(dto);
        bookRepository.save(entity);
    }

    public void saveAllBooks(List<BookDto> dtoList) {
        List<Book> entities = dtoList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        bookRepository.saveAll(entities);
    }

    public BookDto findBookByIsbn(String isbn) {
        return bookRepository.findById(isbn)
                .map(this::convertToDto)
                .orElse(null);
    }

    private Book convertToEntity(BookDto dto) {
        Book entity = new Book();
        entity.setIsbn(dto.getIsbn());
        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setPublisher(dto.getPublisher());
        entity.setThumbnailUrl(dto.getThumbnailUrl());
        return entity;
    }

    private BookDto convertToDto(Book entity) {
        BookDto dto = new BookDto();
        dto.setIsbn(entity.getIsbn());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setPublisher(entity.getPublisher());
        dto.setThumbnailUrl(entity.getThumbnailUrl());
        return dto;
    }
}