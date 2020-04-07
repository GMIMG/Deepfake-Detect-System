package com.gmimg.multicampus.springboot.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByNameLike(String name);
}