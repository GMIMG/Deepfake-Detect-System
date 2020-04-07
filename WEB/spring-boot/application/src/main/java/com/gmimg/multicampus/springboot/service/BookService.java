package com.gmimg.multicampus.springboot.service;

import java.util.Optional;

import com.gmimg.multicampus.springboot.domain.Book;

public interface BookService {

    Optional<Book> findById(Long id);
}